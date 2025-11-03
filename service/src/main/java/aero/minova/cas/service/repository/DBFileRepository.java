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
	/** Delivers the first matching entry (if any).
	 */
    Optional<DBFile> findFirstByKeyTextLikeAndActiveTrue(String fileName);
    
    /** 
     * Delivers the last matching entry (if any).
     */
    Optional<DBFile> findTopByKeyTextLikeAndActiveTrueOrderByKeyTextDesc(String fileName);
    
    /** Find entries, that contain the given infix in the path, e.g. "/images/"
     */
    List<DBFile> findByKeyTextContainingAndActiveTrue(String infix);
    
    /** Get the first entry (if any). E.g. useful to detect the application
     */
    Optional<DBFile> findFirstByActiveTrue();
    
    @Query("SELECT f FROM DBFile f WHERE f.keyText LIKE %:fileName AND f.active = true")
    Optional<DBFileMD5View> findFirstMD5OnlyByFileName(@Param("fileName") String fileName);
}
