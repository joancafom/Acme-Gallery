
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Exhibition;
import domain.Group;
import domain.Guide;
import domain.Museum;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	// ---------------------------------------- QUERY #1 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select avg(d.museums.size) from Director d")
	Double avgMuseumsPerDirector();

	// v1.0 - Alicia
	@Query("select min(d.museums.size) from Director d")
	Double minMuseumsPerDirector();

	// v1.0 - Alicia
	@Query("select max(d.museums.size) from Director d")
	Double maxMuseumsPerDirector();

	// v1.0 - Alicia
	@Query("select sqrt(sum(d.museums.size * d.museums.size) / count(d.museums.size) - avg(d.museums.size) * avg(d.museums.size)) from Director d")
	Double stdMuseumsPerDirector();

	// ---------------------------------------- QUERY #2 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select e.room.museum.id from Exhibition e where e.isPrivate = false group by e.room.museum.id")
	Collection<Integer> museumWithPublicExhibitions();

	// v1.0 - Alicia
	@Query("select count(e) from Exhibition e where e.room.museum.id = ?1 and e.isPrivate = false")
	Double publicExhibitionsByMuseumId(int museumId);

	// v1.0 - Alicia
	@Query("select count(e) from Exhibition e where e.room.museum.id = ?1 and e.isPrivate = true")
	Double privateExhibitionsByMuseumId(int museumId);

	// ---------------------------------------- QUERY #3 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.isPrivate = true order by e.dayPasses.size desc")
	Collection<Exhibition> top5MoreVisitedPrivateExhibitions();

	// v1.0 - Alicia
	@Query("select e from Exhibition e where e.isPrivate = true order by e.dayPasses.size desc")
	Page<Exhibition> top5MoreVisitedPrivateExhibitions(Pageable pageable);

	// ---------------------------------------- QUERY #4 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select avg(dp.price) from DayPass dp where dp.exhibition.isPrivate = true")
	Double avgPricePrivateDayPasses();

	// v1.0 - Alicia
	@Query("select min(dp.price) from DayPass dp where dp.exhibition.isPrivate = true")
	Double minPricePrivateDayPasses();

	// v1.0 - Alicia
	@Query("select max(dp.price) from DayPass dp where dp.exhibition.isPrivate = true")
	Double maxPricePrivateDayPasses();

	// v1.0 - Alicia
	@Query("select sqrt(sum(dp.price * dp.price) / count(dp.price) - avg(dp.price) * avg(dp.price)) from DayPass dp where dp.exhibition.isPrivate = true")
	Double stdPricePrivateDayPasses();

	// ---------------------------------------- QUERY #5 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select avg(m.dayPasses.size) from Museum m")
	Double avgDayPassesPerMuseum();

	// v1.0 - Alicia
	@Query("select min(m.dayPasses.size) from Museum m")
	Double minDayPassesPerMuseum();

	// v1.0 - Alicia
	@Query("select max(m.dayPasses.size) from Museum m")
	Double maxDayPassesPerMuseum();

	// v1.0 - Alicia
	@Query("select sqrt(sum(m.dayPasses.size * m.dayPasses.size) / count(m.dayPasses.size) - avg(m.dayPasses.size) * avg(m.dayPasses.size)) from Museum m")
	Double stdDayPassesPerMuseum();

	// ---------------------------------------- QUERY #6 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select count(s1)*1.0 / (select count(s2)*1.0 from Sponsorship s2) from Sponsorship s1 where s1.status = 'REJECTED'")
	Double ratioRejectedSponsorships();

	// ---------------------------------------- QUERY #7 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select count(s1)*1.0 / (select count(s2)*1.0 from Sponsorship s2) from Sponsorship s1 where s1.status = 'ACCEPTED'")
	Double ratioAcceptedSponsorships();

	// ---------------------------------------- QUERY #8 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select e1 from Exhibition e1 where e1.sponsorships.size > (select avg(e2.sponsorships.size)*1.1 from Exhibition e2)")
	Collection<Exhibition> exhibitions10MoreSponsorshipsThanAvg();

	// v1.0 - Alicia
	@Query("select e1 from Exhibition e1 where e1.sponsorships.size > (select avg(e2.sponsorships.size)*1.1 from Exhibition e2)")
	Page<Exhibition> exhibitions10MoreSponsorshipsThanAvg(Pageable pageable);

	// ---------------------------------------- QUERY #9 ----------------------------------------------

	// v1.0 - Alicia
	@Query("select g from Guide g order by g.exhibitions.size asc")
	Collection<Guide> top3GuidesLessExhibitions();

	// v1.0 - Alicia
	@Query("select g from Guide g order by g.exhibitions.size asc")
	Page<Guide> top3GuidesLessExhibitions(Pageable pageable);

	// ---------------------------------------- QUERY #10 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select avg(e.critiques.size) from Exhibition e")
	Double avgCritiquesPerExhibition();

	// v1.0 - Alicia
	@Query("select sqrt(sum(e.critiques.size * e.critiques.size) / count(e.critiques.size) - avg(e.critiques.size) * avg(e.critiques.size)) from Exhibition e")
	Double stdCritiquesPerExhibition();

	// ---------------------------------------- QUERY #11 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select avg(e.artworks.size) from Exhibition e")
	Double avgArtworksPerExhibition();

	// v1.0 - Alicia
	@Query("select sqrt(sum(e.artworks.size * e.artworks.size) / count(e.artworks.size) - avg(e.artworks.size) * avg(e.artworks.size)) from Exhibition e")
	Double stdArtworksPerExhibition();

	// ---------------------------------------- QUERY #12 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select (select count(v1)*1.0 from Visitor v1 where v1.userAccount.isLocked = true) / count(v2)*1.0 from Visitor v2")
	Double ratioBannedVisitors();

	// ---------------------------------------- QUERY #13 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select (select count(s1)*1.0 from Sponsor s1 where s1.userAccount.isLocked = true) / count(s2)*1.0 from Sponsor s2")
	Double ratioBannedSponsors();

	// ---------------------------------------- QUERY #14 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select avg(g.participants.size) from Group g where g.isClosed = false")
	Double avgParticipantsPerOpenGroup();

	// v1.0 - Alicia
	@Query("select sqrt(sum(g.participants.size * g.participants.size) / count(g.participants.size) - avg(g.participants.size) * avg(g.participants.size)) from Group g where g.isClosed = false")
	Double stdParticipantsPerOpenGroup();

	// ---------------------------------------- QUERY #15 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select (select count(v1)*1.0 from Visitor v1 where v1.createdGroups.size > 0) / count(v2)*1.0 from Visitor v2")
	Double ratioVisitorsCreatedGroups();

	// ---------------------------------------- QUERY #16 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select g1 from Group g1 where g1.announcements.size > (select avg(g2.announcements.size)*1.75 from Group g2)")
	Collection<Group> groups75MoreAnnouncementsThanAvg();

	// v1.0 - Alicia
	@Query("select g1 from Group g1 where g1.announcements.size > (select avg(g2.announcements.size)*1.75 from Group g2)")
	Page<Group> groups75MoreAnnouncementsThanAvg(Pageable pageable);

	// ---------------------------------------- QUERY #17 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select avg(c.childrenComments.size) from Comment c")
	Double avgRepliesPerComment();

	// v1.0 - Alicia
	@Query("select sqrt(sum(c.childrenComments.size * c.childrenComments.size) / count(c.childrenComments.size) - avg(c.childrenComments.size) * avg(c.childrenComments.size)) from Comment c")
	Double stdRepliesPerComment();

	// ---------------------------------------- QUERY #18 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select (select count(m1)*1.0 from Museum m1 where m1.store != null) / count(m2)*1.0 from Museum m2")
	Double ratioMuseumsWithStore();

	// ---------------------------------------- QUERY #19 ---------------------------------------------

	// v1.0 - Alicia
	@Query("select i.room.museum from Incident i where i.level = 'HIGH' group by i.room.museum.id order by count(i) desc")
	Collection<Museum> top3MuseumsHighIncidents();

	// v1.0 - Alicia
	@Query("select i.room.museum from Incident i where i.level = 'HIGH' group by i.room.museum.id order by count(i) desc")
	Page<Museum> top3MuseumsHighIncidents(Pageable pageable);
}
