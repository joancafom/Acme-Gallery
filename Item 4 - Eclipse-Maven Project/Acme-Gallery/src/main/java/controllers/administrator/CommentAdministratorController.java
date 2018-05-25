
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import services.AdministratorService;
import services.CommentService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Comment;

@Controller
@RequestMapping("/comment/administrator")
public class CommentAdministratorController extends AbstractController {

	private final String			ACTOR_WS	= "administrator/";

	//Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private CommentService			commentService;


	/* v1.0 - josembell */
	@RequestMapping(value = "/listTaboo", method = RequestMethod.GET)
	public ModelAndView listTabooed(@RequestParam(value = "d-1332617-p", defaultValue = "1") final Integer page) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Comment> pageResult = this.commentService.findTabooed(page, 5);
		final Collection<Comment> comments = pageResult.getContent();
		final Integer resultSize = new Long(pageResult.getTotalElements()).intValue();

		result = new ModelAndView("comment/list");
		result.addObject("comments", comments);
		result.addObject("resultSize", resultSize);
		result.addObject("actorWS", this.ACTOR_WS);

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int commentId, final RedirectAttributes redirectAttributes) {
		final ModelAndView result;
		final Administrator admin = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Comment comment = this.commentService.findOne(commentId);
		Assert.notNull(comment);

		result = new ModelAndView("redirect:listTaboo.do");

		try {
			this.commentService.delete(comment);
		} catch (final Throwable oops) {
			redirectAttributes.addFlashAttribute("message", "comment.commit.error");
		}

		return result;
	}

}
