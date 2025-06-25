package aero.minova.cas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SelfProbingService {

	@Scheduled(cron = "${self.probing.cron:0 * * * * *}")
	private void run() {

	}
}
