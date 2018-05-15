
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SponsorshipRepository;
import security.LoginService;
import domain.Director;
import domain.Exhibition;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	//Managed Repository
	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	//Supporting Services
	@Autowired
	private DirectorService			directorService;


	//CRUD Methods

	//Other Business Methods

	//v1.0 - Implemented by JA
	public Collection<Sponsorship> findAllByExhibition(final Exhibition exhibition) {

		Assert.notNull(exhibition);

		//This listing may only be accessed by the corresponding director
		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		Assert.notNull(exhibition.getRoom());
		Assert.notNull(exhibition.getRoom().getMuseum());
		Assert.notNull(exhibition.getRoom().getMuseum().getDirector());
		Assert.isTrue(currentDirector.equals(exhibition.getRoom().getMuseum().getDirector()));

		return this.sponsorshipRepository.findAllByExhibitionId(exhibition.getId());
	}

	public Page<Sponsorship> findAllByExhibition(final Exhibition exhibition, final Integer page, final int size) {

		Assert.notNull(exhibition);

		//This listing may only be accessed by the corresponding director
		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		Assert.notNull(exhibition.getRoom());
		Assert.notNull(exhibition.getRoom().getMuseum());
		Assert.notNull(exhibition.getRoom().getMuseum().getDirector());
		Assert.isTrue(currentDirector.equals(exhibition.getRoom().getMuseum().getDirector()));

		return this.sponsorshipRepository.findAllByExhibitionId(exhibition.getId(), new PageRequest(page - 1, size));
	}

}
