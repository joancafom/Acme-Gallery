
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import security.LoginService;
import domain.Director;
import domain.Exhibition;
import domain.Sponsor;
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

	@Autowired
	private ExhibitionService		exhibitionService;

	//Validator
	@Autowired
	private Validator				validator;


	//CRUD Methods

	//v1.0 - Implemented by JA
	public Sponsorship create(final Sponsor sponsor) {

		Assert.notNull(sponsor);

		final Sponsorship res = new Sponsorship();

		res.setSponsor(sponsor);
		sponsor.getSponsorships().add(res);

		return res;
	}

	//v1.0 - Implemented by JA
	public Sponsorship findOne(final int sponsorhipId) {

		return this.sponsorshipRepository.findOne(sponsorhipId);
	}

	//v1.0 - Implemented by JA
	public Sponsorship save(final Sponsorship sponsorship) {

		Assert.notNull(sponsorship);

		//Basic save method

		//The following assertions are intended that the sponsorship 
		//has a valid combination of values (according to the statement)
		//and thus they must be true at any moment independent of the current operation

		final Date now = new Date();

		if (sponsorship.getStartingDate() != null) {
			Assert.isTrue(now.after(sponsorship.getStartingDate()));
			Assert.notNull(sponsorship.getEndingDate());
		}

		if (sponsorship.getEndingDate() != null) {
			Assert.isTrue(now.after(sponsorship.getEndingDate()));
			Assert.notNull(sponsorship.getStartingDate());

			Assert.isTrue(sponsorship.getStartingDate().before(sponsorship.getEndingDate()));
		}

		Assert.notNull(sponsorship.getStatus());

		if (sponsorship.getId() == 0)
			Assert.isTrue(sponsorship.getStatus().equals("PENDING"));

		if (sponsorship.getStatus().equals("ACCEPTED")) {
			Assert.notNull(sponsorship.getCreditCard());
			Assert.notNull(sponsorship.getCreditCard().getNumber());
			Assert.notNull(sponsorship.getEndingDate());
			Assert.notNull(sponsorship.getStartingDate());
		} else if (sponsorship.getStatus().equals("PENDING")) {
			Assert.isTrue(sponsorship.getCreditCard() == null || sponsorship.getCreditCard().getNumber() == null);
			Assert.isNull(sponsorship.getEndingDate());
			Assert.isNull(sponsorship.getStartingDate());
		} else if (sponsorship.getStatus().equals("TIME_NEGOTIATION")) {
			Assert.isTrue(sponsorship.getCreditCard() == null || sponsorship.getCreditCard().getNumber() == null);
			Assert.notNull(sponsorship.getEndingDate());
			Assert.notNull(sponsorship.getStartingDate());
		} else if (sponsorship.getStatus().equals("REJECTED"))
			Assert.isTrue(sponsorship.getCreditCard() == null || sponsorship.getCreditCard().getNumber() == null);

		final Sponsorship savedSponsorship = this.sponsorshipRepository.save(sponsorship);

		if (sponsorship.getId() == 0) {
			savedSponsorship.getExhibition().getSponsorships().add(savedSponsorship);
			this.exhibitionService.save(savedSponsorship.getExhibition());
		}

		return savedSponsorship;
	}
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

	//v1.0 - Implemented by JA
	public Sponsorship updateStatus(final Sponsorship sponsorshipToUpdate) {

		Assert.notNull(sponsorshipToUpdate);
		Assert.isTrue(sponsorshipToUpdate.getId() != 0);

		//First check that no other fields are changed
		final Sponsorship oldSponsorship = this.findOne(sponsorshipToUpdate.getId());
		Assert.notNull(oldSponsorship);

		Assert.isTrue(oldSponsorship.getSponsor().equals(sponsorshipToUpdate.getSponsor()));
		Assert.isNull(oldSponsorship.getCreditCard());
		Assert.isNull(sponsorshipToUpdate.getCreditCard());
		Assert.isTrue(oldSponsorship.getBanner().equals(sponsorshipToUpdate.getBanner()));
		Assert.isTrue(oldSponsorship.getLink().equals(sponsorshipToUpdate.getLink()));
		Assert.isNull(oldSponsorship.getStartingDate());
		Assert.isNull(oldSponsorship.getEndingDate());
		Assert.isTrue(oldSponsorship.getExhibition().equals(sponsorshipToUpdate.getExhibition()));
		Assert.isTrue(oldSponsorship.getStatus().equals("PENDING"));

		//Ensure the performer is the director of the exhibition the sponsorship references to.
		final Director currentDirector = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(currentDirector);

		//We skip the sequence of notNull checks as it is the oldSponsorship, that is persisted in the DB
		//and thus must be consistent with the domain model (all fields are mandatory, not null)
		//We can do this on the old one as we have already checked that both Exhibitions are equal
		Assert.notNull(oldSponsorship.getExhibition().getRoom().getMuseum().getDirector().equals(currentDirector));

		//A Director may only change it to TIME_NEGOTIATION or REJECTED
		Assert.isTrue(sponsorshipToUpdate.getStatus().equals("TIME_NEGOTIATION") || sponsorshipToUpdate.getStatus().equals("REJECTED"));

		return this.save(sponsorshipToUpdate);
	}

	//v1.0 - Implemented by JA
	public Sponsorship reconstructUpdateStatus(final Sponsorship sponsorshipToReconstruct, final BindingResult binding) {

		Assert.notNull(sponsorshipToReconstruct);
		Assert.notNull(sponsorshipToReconstruct.getId() != 0);

		final Sponsorship oldSponsorship = this.findOne(sponsorshipToReconstruct.getId());
		Assert.notNull(oldSponsorship);

		final Sponsorship res = this.create(oldSponsorship.getSponsor());
		res.setExhibition(oldSponsorship.getExhibition());
		res.setBanner(oldSponsorship.getBanner());
		res.setCreditCard(oldSponsorship.getCreditCard());
		res.setLink(oldSponsorship.getLink());
		res.setStartingDate(sponsorshipToReconstruct.getStartingDate());
		res.setEndingDate(sponsorshipToReconstruct.getEndingDate());
		res.setStatus(sponsorshipToReconstruct.getStatus());
		res.setId(oldSponsorship.getId());
		res.setVersion(oldSponsorship.getVersion());

		this.validator.validate(res, binding);

		return res;
	}
}
