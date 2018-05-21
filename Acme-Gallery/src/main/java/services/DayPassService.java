
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.DayPassRepository;
import security.LoginService;
import domain.DayPass;
import domain.Director;
import domain.Exhibition;
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

		dayPass.setPrice(priceWithTaxes);
		dayPass.setVAT(this.systemConfigurationService.getCurrentSystemConfiguration().getVAT());

		dayPass.setVisitor(visitor);
		dayPass.setExhibition(exhibition);
		dayPass.setMuseum(exhibition.getRoom().getMuseum());

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

	// v1.0 - Alicia
	public DayPass saveCreateAndEdit(final DayPass dayPass) {
		Assert.notNull(dayPass);

		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(visitor);

		return null;

	}

	// Other Business Methods -------------------------------------------------------------------------

	// v1.0 - Alicia
	public Boolean canBuyADayPass(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		Boolean res = false;

		if (exhibition.getIsPrivate() && exhibition.getEndingDate().after(new Date()))
			res = true;

		return res;

	}

	// v1.0 - Alicia
	public DayPass reconstruct(final DayPass prunedDayPass, final BindingResult binding) {
		Assert.notNull(prunedDayPass);

		final DayPass res = this.create(prunedDayPass.getExhibition());
		final Visitor visitor = this.visitorService.findByUserAccount(LoginService.getPrincipal());

		res.setTicker(prunedDayPass.getMuseum().getIdentifier() + "-" + visitor.getUserAccount().getUsername() + "-" + prunedDayPass.getMuseum().getDayPasses().size());
		res.setPurchaseMoment(new Date(System.currentTimeMillis() - 1000));
		res.setVisitDate(prunedDayPass.getVisitDate());
		res.setCreditCard(prunedDayPass.getCreditCard());
		res.setPrice(prunedDayPass.getPrice());
		res.setVAT(prunedDayPass.getVAT());

		res.setVisitor(prunedDayPass.getVisitor());
		res.setExhibition(prunedDayPass.getExhibition());
		res.setMuseum(prunedDayPass.getMuseum());

		this.validator.validate(res, binding);

		return res;
	}
}
