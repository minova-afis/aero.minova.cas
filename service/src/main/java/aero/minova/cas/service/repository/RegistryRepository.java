package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.RegistryEntry;

@Repository
public interface RegistryRepository extends CrudRepository<RegistryEntry, Integer> {
	/**
	 * Delivers the first matching entry (if any).
	 */
	Optional<RegistryEntry> findFirstByKeyTextLikeAndActiveTrue(String keyPath);
	
	/**
	 * Delivers the first matching entry (if any).
	 */
	Optional<RegistryEntry> findFirstByKeyTextAndActiveTrue(String keyPath);

	/**
	 * Delivers the last matching entry (if any).
	 */
	Optional<RegistryEntry> findTopByKeyTextLikeAndActiveTrueOrderByKeyTextDesc(String keyPath);

	/**
	 * Find entries, that contain the given infix in the path, e.g. "/afis/JobExecutor/"
	 */
	List<RegistryEntry> findByKeyTextContainingAndActiveTrue(String infix);

	/**
	 * Get the first entry (if any). E.g. useful to detect the application
	 */
	Optional<RegistryEntry> findFirstByActiveTrue();

	/**
	 * Get all active entries
	 */
	List<RegistryEntry> findAllByActiveTrue();
	
	/** Deliver all values that start with given text
	 * e.g. findByKeyTextStartingWithAndActiveTrue("/afis/") if converted to
	 *    SELECT * FROM tRegistry WHERE KeyText LIKE '/afis/%' AND Active = true
	 */
	List<RegistryEntry> findByKeyTextStartingWithAndActiveTrue(String prefix);
}
