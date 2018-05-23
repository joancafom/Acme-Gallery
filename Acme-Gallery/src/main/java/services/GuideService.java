
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.GuideRepository;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import domain.Exhibition;
import domain.Guide;
import domain.Incident;
import forms.ActorRegistrationForm;

@Service
@Transactional
public class GuideService extends ActorService {

	// Managed Repository -----------------------------------------------------------------------------

	@Autowired
	private GuideRepository			guideRepository;

	// Supporting Services ----------------------------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	// Validator --------------------------------------------------------------------------------------

	// CRUD Methods -----------------------------------------------------------------------------------

	// v1.0 - Alicia
	public Guide create() {
		final Guide res = super.create(Guide.class);

		res.setIncidents(new HashSet<Incident>());
		res.setExhibitions(new HashSet<Exhibition>());

		return res;
	}

	// v1.0 - Alicia
	public Guide findOne(final int guideId) {
		return this.guideRepository.findOne(guideId);
	}

	// v1.0 - Alicia
	public Guide save(final Guide guide) {
		Assert.notNull(guide);

		return this.guideRepository.save(guide);
	}

	//Other Business Methods --------------------------------------------------------------------------

	// v1.0 - JA
	public Collection<Guide> findAllByExhibition(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		final Collection<Guide> res = this.guideRepository.findAllByExhibition(exhibition);
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Page<Guide> findAllByExhibition(final Exhibition exhibition, final int page, final int size) {
		Assert.notNull(exhibition);

		final Page<Guide> res = this.guideRepository.findAllByExhibition(exhibition, new PageRequest(page - 1, size));
		Assert.notNull(res);

		return res;
	}

	// v1.0 - JA
	public Guide findByUserAccount(final UserAccount userAccount) {

		Assert.notNull(userAccount);

		return super.findByUserAccount(userAccount, Guide.class);
	}

	// v1.0 - Alicia
	public Guide reconstructRegisterForm(final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {
		final Guide res = this.create();

		return this.reconstructRegisterForm(res, actorRegistrationForm, binding);
	}

	// v1.0 - Alicia
	public Guide register(final Guide guide) {
		Assert.notNull(guide);

		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(administrator);

		Assert.isTrue(guide.getId() == 0);
		Assert.notNull(guide.getUserAccount());
		Assert.notNull(guide.getUserAccount().getPassword());

		//HashPassword
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(guide.getUserAccount().getPassword(), null);
		guide.getUserAccount().setPassword(hashedPassword);

		return this.save(guide);
	}

	// v1.0 - Alicia
	public Collection<Guide> getNotWorkingInExhibition(final Exhibition exhibition) {
		Assert.notNull(exhibition);

		final Collection<Guide> res = this.guideRepository.findByMuseumIdNotInExhibition(exhibition.getRoom().getMuseum().getId(), exhibition);
		Assert.notNull(res);

		return res;
	}

}
