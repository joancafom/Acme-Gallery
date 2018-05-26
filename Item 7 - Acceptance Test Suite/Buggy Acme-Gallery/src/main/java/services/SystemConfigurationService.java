
package services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SystemConfigurationRepository;
import security.LoginService;
import domain.Administrator;
import domain.Announcement;
import domain.Comment;
import domain.Group;
import domain.Review;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	//Managed Repository -------------------------------------

	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	// Supporting Services -----------------------------------

	@Autowired
	private AdministratorService			adminService;

	@Autowired
	private GroupService					groupService;

	@Autowired
	private ReviewService					reviewService;

	@Autowired
	private CommentService					commentService;

	@Autowired
	private AnnouncementService				announcementService;

	//Validator
	@Autowired
	private Validator						validator;


	// CRUD Methods -----------------------------------

	//v1.0 - Implemented by JA
	public SystemConfiguration create() {

		final SystemConfiguration sysConfig = new SystemConfiguration();

		return sysConfig;
	}

	//v1.0 - Implemented by JA
	public SystemConfiguration findOne(final int systemConfigurationId) {

		return this.systemConfigurationRepository.findOne(systemConfigurationId);
	}

	//v1.0 - Implemented by JA
	public Collection<SystemConfiguration> findAll() {

		final Collection<SystemConfiguration> res = this.systemConfigurationRepository.findAll();

		Assert.notNull(res);
		if (res.size() > 1)
			throw new IllegalStateException("There cannot be more than one systemConfiguration at the time");

		return res;
	}

	//v1.0 - Implemented by JA
	public SystemConfiguration save(final SystemConfiguration sC) {

		Assert.notNull(sC);

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		//Will only consist of one element
		final Collection<SystemConfiguration> allSCs = this.findAll();

		//Guarantee the uniqueness
		if (allSCs != null)
			for (final SystemConfiguration sysConfig : allSCs)
				if (!sysConfig.equals(sC))
					this.delete(sysConfig);

		return this.systemConfigurationRepository.save(sC);
	}

	//v1.0 - Implemented by JA
	public void delete(final SystemConfiguration sC) {

		Assert.notNull(sC);
		Assert.isTrue(this.systemConfigurationRepository.exists(sC.getId()));

		this.systemConfigurationRepository.delete(sC);
	}

	//Other Business Methods ------------------------------

	//v1.0 - Implemented by JA
	public Boolean containsTaboo(final String testString) {

		Assert.notNull(testString);
		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		Assert.notNull(sysConfig);

		Boolean res;

		final Pattern p = Pattern.compile(sysConfig.getTabooWords().toLowerCase());

		if (p.toString() != "") {
			final Matcher veredict = p.matcher(testString.toLowerCase());
			res = veredict.find();
		} else
			res = false;

		return res;
	}

	// v1.0 - Implemented by JA
	public void flush() {
		this.systemConfigurationRepository.flush();
	}

	//v1.0 - Implemented by JA
	public SystemConfiguration getCurrentSystemConfiguration() {

		//Theoretically there is only one SystemConfiguration in our system, so a findAll operation
		//is not an overhead

		final Collection<SystemConfiguration> allSysConfig = this.findAll();
		SystemConfiguration res;

		res = allSysConfig.isEmpty() ? null : allSysConfig.iterator().next();

		return res;
	}

	//v1.0 - Implemented by JA
	public Collection<String> getTabooWords() {

		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		Assert.notNull(sysConfig);

		final String tabooWords = sysConfig.getTabooWords();
		Assert.notNull(tabooWords);

		final Collection<String> collectionTabooWords = new HashSet<String>();

		if (!tabooWords.equals("")) {
			final String[] tabooWordsSplitted = tabooWords.split("\\|");
			collectionTabooWords.addAll(Arrays.asList(tabooWordsSplitted));
		}

		return collectionTabooWords;
	}

	// v1.0 - Implemented by JA
	public String addTabooWord(final String tabooWordIn) {

		Assert.notNull(tabooWordIn);
		Assert.isTrue(!tabooWordIn.equals(""));

		final String tabooWord = tabooWordIn.toLowerCase();
		Assert.isTrue(!this.getTabooWords().contains(tabooWord));
		Assert.isTrue(!tabooWord.contains("|"));

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		Assert.notNull(sysConfig);

		String tabooWords = sysConfig.getTabooWords();

		if (tabooWords.equals(""))
			tabooWords = tabooWord;
		else
			tabooWords = tabooWords + "|" + tabooWord;

		sysConfig.setTabooWords(tabooWords);
		this.save(sysConfig);

		final Collection<Group> notTabooedGroups = this.groupService.findNotTabooed();
		final Collection<Announcement> notTabooedAnnouncements = this.announcementService.findNotTabooed();
		final Collection<Comment> notTabooedComments = this.commentService.findNotTabooed();
		final Collection<Review> notTabooedReviews = this.reviewService.findNotTabooed();

		this.updateTabooElements(tabooWords, notTabooedGroups, notTabooedAnnouncements, notTabooedComments, notTabooedReviews);

		return tabooWords;
	}

	// v1.0 - Implemented by JA
	public String deleteTabooWord(final String tabooWord) {

		Assert.notNull(tabooWord);
		Assert.isTrue(!tabooWord.contains("|"));
		Assert.isTrue(this.getTabooWords().contains(tabooWord));

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final SystemConfiguration sysConfig = this.getCurrentSystemConfiguration();
		Assert.notNull(sysConfig);

		String tabooWords = sysConfig.getTabooWords();

		tabooWords = "|" + tabooWords + "|";

		tabooWords = tabooWords.replaceAll("\\|" + tabooWord + "\\|", "|");

		if (tabooWords.equals("|"))
			tabooWords = "";
		else
			tabooWords = tabooWords.substring(1, tabooWords.length() - 1);

		sysConfig.setTabooWords(tabooWords);
		this.save(sysConfig);

		final Collection<Group> tabooedGroups = this.groupService.findTabooed();
		final Collection<Announcement> tabooedAnnouncements = this.announcementService.findTabooed();
		final Collection<Comment> tabooedComments = this.commentService.findTabooed();
		final Collection<Review> tabooedReviews = this.reviewService.findTabooed();

		this.updateTabooElements(tabooWords, tabooedGroups, tabooedAnnouncements, tabooedComments, tabooedReviews);

		return tabooWords;
	}

	public SystemConfiguration reconstructEdit(final SystemConfiguration prunedSC, final BindingResult binding) {

		Assert.notNull(prunedSC);
		Assert.notNull(binding);

		final SystemConfiguration currentSC = this.getCurrentSystemConfiguration();
		Assert.notNull(currentSC);

		final SystemConfiguration res = prunedSC;
		res.setTabooWords(currentSC.getTabooWords());

		this.validator.validate(prunedSC, binding);

		return res;

	}

	private void updateTabooElements(final String tabooWords, final Collection<Group> groupsToUpdate, final Collection<Announcement> announcementsToUpdate, final Collection<Comment> commentsToUpdate, final Collection<Review> reviewsToUpdate) {

		for (final Group g : groupsToUpdate)
			this.groupService.save(g);

		for (final Announcement a : announcementsToUpdate)
			this.announcementService.save(a);

		for (final Comment c : commentsToUpdate)
			this.commentService.save(c);

		for (final Review r : reviewsToUpdate)
			this.reviewService.save(r);

	}
}
