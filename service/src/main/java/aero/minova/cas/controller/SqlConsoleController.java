package aero.minova.cas.controller;

import aero.minova.cas.sql.SystemDatabase;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DEVELOPER TOOL — executes arbitrary SQL against the application database.
 *
 * <p>This controller is <b>ONLY registered</b> when the application property
 * {@code ng.api.activateSUexecuteSQL=true} is explicitly set.
 * It is disabled by default and must <b>NEVER</b> be enabled in production environments.</p>
 *
 * <p>Endpoint: {@code POST /data/execute-sql}</p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ng.api.activateSUexecuteSQL", havingValue = "true")
public class SqlConsoleController {

    /** Maximum rows returned for SELECT queries to prevent memory exhaustion. */
    private static final int MAX_ROWS = 500;

    private final SystemDatabase systemDatabase;

    // ── Request / Response DTOs ────────────────────────────────────────────────

    @Data
    @NoArgsConstructor
    public static class SqlConsoleRequest {
        private String sql;
    }

    @Data
    @NoArgsConstructor
    public static class SqlConsoleResponse {
        private boolean success;
        /** Human-readable OK message or SQL error text. Null for SELECT results. */
        private String message;
        /** Column labels — only populated for SELECT results. */
        private List<String> columns;
        /** Row values as strings (null for SQL NULL). Only populated for SELECT results. */
        private List<List<String>> rows;
        /** True when rows were cut to MAX_ROWS. */
        private boolean truncated;
        /** Number of rows in {@code rows}, or rows-affected count for DML/DDL. */
        private int rowCount;

        static SqlConsoleResponse error(String msg) {
            SqlConsoleResponse r = new SqlConsoleResponse();
            r.success = false;
            r.message = msg;
            return r;
        }

        static SqlConsoleResponse ok(String msg, int count) {
            SqlConsoleResponse r = new SqlConsoleResponse();
            r.success = true;
            r.message = msg;
            r.rowCount = count;
            return r;
        }

        static SqlConsoleResponse result(List<String> columns, List<List<String>> rows, boolean truncated) {
            SqlConsoleResponse r = new SqlConsoleResponse();
            r.success = true;
            r.columns = columns;
            r.rows = rows;
            r.truncated = truncated;
            r.rowCount = rows.size();
            return r;
        }
    }

    // ── Endpoint ───────────────────────────────────────────────────────────────

    @PostMapping(value = "data/execute-sql", produces = "application/json")
    public ResponseEntity<SqlConsoleResponse> executeSql(
            @RequestBody SqlConsoleRequest request,
            Principal principal) {

        if (principal == null || !"admin".equals(principal.getName())) {
            log.warn("data/execute-sql: rejected — caller '{}' is not admin",
                    principal != null ? principal.getName() : "<unauthenticated>");
            return ResponseEntity.status(403)
                    .body(SqlConsoleResponse.error("Access denied: admin login required"));
        }

        final String sql = request.getSql() != null ? request.getSql().trim() : "";
        log.info("data/execute-sql: {}", sql.length() > 120 ? sql.substring(0, 120) + "…" : sql);

        if (sql.isEmpty()) {
            return ResponseEntity.ok(SqlConsoleResponse.error("SQL is empty"));
        }

        try (Connection conn = systemDatabase.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                final boolean hasResultSet = stmt.execute(sql);

                if (hasResultSet) {
                    try (ResultSet rs = stmt.getResultSet()) {
                        final ResultSetMetaData meta = rs.getMetaData();
                        final int colCount = meta.getColumnCount();

                        final List<String> columns = new ArrayList<>(colCount);
                        for (int i = 1; i <= colCount; i++) {
                            columns.add(meta.getColumnLabel(i));
                        }

                        final List<List<String>> rows = new ArrayList<>();
                        boolean truncated = false;
                        while (rs.next()) {
                            if (rows.size() >= MAX_ROWS) {
                                truncated = true;
                                break;
                            }
                            final List<String> row = new ArrayList<>(colCount);
                            for (int i = 1; i <= colCount; i++) {
                                final Object val = rs.getObject(i);
                                row.add(val != null ? val.toString() : null);
                            }
                            rows.add(row);
                        }

                        conn.commit();
                        return ResponseEntity.ok(SqlConsoleResponse.result(columns, rows, truncated));
                    }
                } else {
                    final int updateCount = stmt.getUpdateCount();
                    conn.commit();
                    final String msg = updateCount >= 0 ? updateCount + " row(s) affected" : "OK";
                    return ResponseEntity.ok(SqlConsoleResponse.ok(msg, Math.max(updateCount, 0)));
                }

            } catch (Exception e) {
                try { conn.rollback(); } catch (Exception ignored) { }
                log.warn("data/execute-sql: SQL error", e);
                return ResponseEntity.ok(SqlConsoleResponse.error(e.getMessage()));
            }
        } catch (Exception e) {
            log.error("data/execute-sql: connection error", e);
            return ResponseEntity.ok(SqlConsoleResponse.error(e.getMessage()));
        }
    }
}
