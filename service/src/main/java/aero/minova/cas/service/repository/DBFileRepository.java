package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.DBFile;
import aero.minova.cas.service.model.DBFileMD5View;

@Repository
public interface DBFileRepository extends CrudRepository<DBFile, String> {
    @Query("SELECT f FROM DBFile f WHERE f.keyText LIKE %:fileName AND f.active = true")
    Optional<DBFile> findActiveFileByFileName(@Param("fileName") String fileName);
    
    /** Find entries, that contain the given infix in the path, e.g. "/images/"
     */
    List<DBFile> findByKeyTextContainingAndActiveTrue(String infix);
    
    /** Get the first entry (if any). E.g. useful to detect the application
     */
    Optional<DBFile> findFirstByActiveTrue();
    
    @Query("SELECT f FROM DBFile f WHERE f.keyText LIKE %:fileName AND f.active = true")
    Optional<DBFileMD5View> findMD5OnlyByFileName(@Param("fileName") String fileName);
}
