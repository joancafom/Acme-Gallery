
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.DayPassRepository;
import security.LoginService;
import domain.DayPass;
import domain.Director;
import domain.Exhibition;
import domain.Museum;
import domain.Visitor;

@Service
@Transactional
public class DayPassService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private DayPassRepository			dayPassRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private VisitorService				visitorService;

	@Autowired
	private DirectorService				directorService;

	@Autowired
	private MuseumService				museumService;

	// Validator --------------------------------------------------------------------------------------

	@Autowired
	private Validator					validator;


	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public DayPass create(final Exhibition exhibition) {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final DayPass dayPass = new DayPass();

		final Double priceWithoutTaxes = exhibition.getRoom().getMuseum().getPrice() + exhibition.getPrice();
		final Double priceWithTaxes = priceWithoutTaxes * (1 + this.systemConfigurationService.getCurrentSystemConfiguration().getVAT());

		final Double finalPrice = Math.floor(priceWithTaxes * 100) / 100;

		dayPass.setPrice(finalPrice);
		dayPass.setVAT(this.systemConfigurationService.getCurrentSystemConfiguration().getVAT());

		dayPass.setVisitor(visitor);
		dayPass.setExhibition(exhibition);
		dayPass.setMuseum(exhibition.getRoom().getMuseum());

		return dayPass;
	}

	//v1.0 - JA
	public Collection<DayPass> findAll() {

		return this.dayPassRepository.findAll();
	}

	// v1.0 - Alicia
	public DayPass create(final Museum museum) {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final DayPass dayPass = new DayPass();

		final Double priceWithoutTaxes = museum.getPrice();
		final Double priceWithTaxes = priceWithoutTaxes * (1 + this.systemConfigurationService.getCurrentSystemConfiguration().getVAT());

		final Double finalPrice = Math.floor(priceWithTaxes * 100) / 100;

		dayPass.setPrice(finalPrice);
		dayPass.setVAT(this.systemConfigurationService.getCurrentSystemConfiguration().getVAT());

		dayPass.setVisitor(visitor);
		dayPass.setMuseum(museum);

		return dayPass;
	}

	// v1.0 - Alicia
	public void delete(final DayPass dayPass) {
		Assert.notNull(dayPass);
		Assert.isTrue(this.dayPassRepository.exists(dayPass.getId()));

		final Director director = this.directorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(director);
		Assert.isTrue(director.getMuseums().contains(dayPass.getMuseum()));
		Assert.notNull(dayPass.getExhibition());
		Assert.isTrue(dayPass.getExhibition().getEndingDate().before(new Date()));

		dayPass.getMuseum().getDayPasses().remove(dayPass);
		this.museumService.save(dayPass.getMuseum());

		dayPass.setExhibition(null);

		this.dayPassRepository.delete(dayPass);
	}

	// v2.0 - Alicia
	public DayPass saveCreateAndEdit(final DayPass dayPass) {
		Assert.notNull(dayPass);
		Assert.isTrue(dayPass.getId() == 0);

		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		Assert.isTrue(visitor.equals(dayPass.getVisitor()));

		if (dayPass.getExhibition() != null) {
			Assert.isTrue(this.canBuyADayPass(dayPass.getExhibition()));

			final int compareStartingDate = DateTimeComparator.getDateOnlyInstance().compare(dayPass.getVisitDate(), dayPass.getExhibition().getStartingDate());
			Assert.isTrue(compareStartingDate >= 0);
			final int compareEndingDate = DateTimeComparator.getDateOnlyInstance().compare(dayPass.getVisitDate(), dayPass.getExhibition().getEndingDate());
			Assert.isTrue(compareEndingDate <= 0);
		} else
			Assert.isTrue(dayPass.getVisitDate().after(new Date()));

		final LocalDate now = new LocalDate();
		Assert.notNull(dayPass.getCreditCard());

		// Assert (year == current && month == current) || year == future || (year == current && month == future)
		Assert.isTrue((now.getYear() == dayPass.getCreditCard().getYear() && now.getMonthOfYear() == dayPass.getCreditCard().getMonth()) || (now.getYear() < dayPass.getCreditCard().getYear())
			|| (now.getYear() == dayPass.getCreditCard().getYear() && now.getMonthOfYear() < dayPass.getCreditCard().getMonth()));

		return this.dayPassRepository.save(dayPass);

	}
	// Other Business Methods -------------------------------------------------------------------------

	// v1.0 - Alicia
	public Boolean canBuyADayPass(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		Boolean res = false;

		if (exhibition.getIsPrivate() && exhibition.getEndingDate().after(new Date()) && !exhibition.getArtworks().isEmpty())
			res = true;

		return res;

	}

	// v3.0 - Alicia
	public DayPass reconstruct(final DayPass prunedDayPass, final BindingResult binding) {
		Assert.notNull(prunedDayPass);
		Assert.isTrue(prunedDayPass.getId() == 0);

		DayPass res = null;
		int number = 0;
		String tickerMuseum = null;

		if (prunedDayPass.getExhibition() != null) {
			res = this.create(prunedDayPass.getExhibition());
			number = prunedDayPass.getExhibition().getRoom().getMuseum().getDayPasses().size();
			tickerMuseum = prunedDayPass.getExhibition().getRoom().getMuseum().getIdentifier();

		} else if (prunedDayPass.getMuseum() != null) {
			res = this.create(prunedDayPass.getMuseum());
			number = prunedDayPass.getMuseum().getDayPasses().size();
			tickerMuseum = prunedDayPass.getMuseum().getIdentifier();
		}

		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final String formatedNumber = String.format("%04d", number);

		res.setTicker(tickerMuseum + "-" + visitor.getUserAccount().getUsername() + "-" + formatedNumber);
		res.setPurchaseMoment(new Date(System.currentTimeMillis() - 1000));
		res.setVisitDate(prunedDayPass.getVisitDate());
		res.setCreditCard(prunedDayPass.getCreditCard());

		res.setVisitor(visitor);

		this.validator.validate(res, binding);

		return res;
	}

	// v1.0 - Alicia
	public Collection<DayPass> getByPrincipal() {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final Collection<DayPass> res = this.dayPassRepository.findByVisitorId(visitor.getId());
		Assert.notNull(res);

		return res;
	}

	// v1.0 - Alicia
	public Page<DayPass> getByPrincipal(final int page, final int size) {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		final Page<DayPass> res = this.dayPassRepository.findByVisitorId(visitor.getId(), new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	/* v1.0 - josembell */
	public Collection<DayPass> findAllByMuseumAndPrincipal(final Museum museum) {
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);
		return this.dayPassRepository.findAllByMuseumAndPrincipal(museum.getId(), visitor.getId());
	}

}
