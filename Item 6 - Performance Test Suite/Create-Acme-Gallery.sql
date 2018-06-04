start transaction;

drop database if exists `Acme-Newspaper`;
create database `Acme-Newspaper`;

use `Acme-Newspaper`;

create user 'acme-user'@'%' identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete 
  on `Acme-Newspaper`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-Newspaper`.* to 'acme-manager'@'%';

-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Gallery
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_idt4b4u259p6vs4pyr9lax4eg` (`userAccount_id`),
  CONSTRAINT `FK_idt4b4u259p6vs4pyr9lax4eg` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (23169,0,'824 Ashton Lane','fran@gmail.com','MALE','Fran','+34512565480','Vail',23122);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aggroup`
--

DROP TABLE IF EXISTS `aggroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aggroup` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `containsTaboo` bit(1) NOT NULL,
  `creationMoment` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `isClosed` bit(1) NOT NULL,
  `maxParticipants` int(11) DEFAULT NULL,
  `meetingDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creator_id` int(11) NOT NULL,
  `museum_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_rvpl7qf0g2vbl7l69ge3ci6va` (`creator_id`),
  KEY `FK_qqjicja707am14qkb22qa0jwr` (`museum_id`),
  CONSTRAINT `FK_qqjicja707am14qkb22qa0jwr` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`id`),
  CONSTRAINT `FK_rvpl7qf0g2vbl7l69ge3ci6va` FOREIGN KEY (`creator_id`) REFERENCES `visitor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aggroup`
--

LOCK TABLES `aggroup` WRITE;
/*!40000 ALTER TABLE `aggroup` DISABLE KEYS */;
INSERT INTO `aggroup` VALUES (23372,0,'\0','2018-04-19 17:32:00','Let\'s have a good time','\0',30,'2018-09-18 10:00:00','Vist to El Prado',23243,23206),(23373,0,'\0','2018-05-05 12:58:00','Let\'s go!!','',10,'2018-06-21 12:30:00','British Museum Meeting',23244,23213),(23374,0,'\0','2018-02-12 13:02:00','It\'s gonna be so romantic!','',2,'2018-02-14 20:00:00','Date in El Prado',23243,23206),(23375,0,'','2018-04-21 19:43:00','We\'re gonna have so much fun together!! (Without sex)','',10,'2018-05-03 16:00:00','The Squad in El Prado!',23243,23206),(23376,0,'\0','2018-04-21 20:22:00','Anyone who\'s interested in going can join us :)','',100,'2018-07-21 10:00:00','Open Meeting to El Prado',23243,23206),(23377,0,'\0','2017-12-17 23:01:00','I\'ll show you what real art is','',5,'2018-02-14 20:00:00','Visit to the British Museum',23243,23213),(23378,0,'\0','2018-03-03 20:31:00','Let\'s have fun together','',10,'2018-03-28 16:00:00','Visit to el Prado without Tricia',23244,23206),(23379,0,'\0','2018-02-01 15:21:00','Does someone wanna come with me?','\0',10,'2019-02-02 12:00:00','Madame Tussauds Meeting',23246,23230),(23380,0,'','2018-02-13 21:05:00','Surprise! Are you excited? Don\'t forget to bring viagra','',2,'2018-02-14 10:00:00','Date to the National Gallery!!',23247,23235),(23381,0,'','2018-02-18 13:11:00','Anyone can come with me? Maybe later we can have sex','\0',100,'2018-02-19 17:00:00','Visiting the National Gallery Tomorrow',23246,23235),(23382,0,'\0','2017-12-25 19:43:00','Any christian who wants to join me? Your sex doesn\'t matter','\0',100,'2018-01-06 19:00:00','Visiting the Vaticano',23243,23218),(23383,0,'','2018-05-08 11:04:00','We\'re going to attend a exposition about Sex','',2,'2018-06-19 20:30:00','Visiting El Prado Alone',23246,23206),(23384,0,'','2018-03-02 10:59:00','VIRUS 2018 FREE SEX PORN VIAGRA','\0',10,'2018-06-05 16:00:00','Visiting Madame Tussauds',23247,23230),(23385,0,'\0','2018-03-24 20:05:00','Little things are also art.','\0',2,'2018-09-05 17:30:00','Modern Art',23246,23206);
/*!40000 ALTER TABLE `aggroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `announcement`
--

DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `announcement` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `containsTaboo` bit(1) NOT NULL,
  `creationMoment` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_p9u35snbda1unqd9dqojt9ojy` (`group_id`),
  CONSTRAINT `FK_p9u35snbda1unqd9dqojt9ojy` FOREIGN KEY (`group_id`) REFERENCES `aggroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcement`
--

LOCK TABLES `announcement` WRITE;
/*!40000 ALTER TABLE `announcement` DISABLE KEYS */;
INSERT INTO `announcement` VALUES (23432,0,'\0','2018-04-19 18:01:00','Please, be kind :)','https://www.thinkgeek.com/images/products/additional/large/jukg_bff_rainbow_cloud_plush_rainbow.jpg','Welcome to the Group',23372),(23433,0,'','2018-04-20 10:22:00','No drug will be allowed during the meeting. That includes pharmaceuticals such as viagra or cialis.','https://www.thesignshed.co.uk/ekmps/shops/yorkie1922/images/no-drugs-sign-29837-p.png','Considerations',23372),(23434,0,'\0','2018-04-25 18:34:00','It\'s predicted that it will be sunny so you don\'t need to bring any scarf or jacket',NULL,'Clothing',23372),(23435,0,'\0','2018-04-27 20:01:00','You can ask any question in the comments below and I will answer as soon as possible',NULL,'Question',23372),(23436,0,'\0','2018-04-28 12:34:00','Museums\' restaurants are usually very expensive. It would be a good idea to bring our own food and eat it in a park near the museum.',NULL,'IMPORTANT: Bring food',23372),(23437,0,'','2018-05-01 02:45:00','Practicing sex is not allowed in the museum. It\'s very important for you to know that',NULL,'Last but not least',23372),(23438,0,'\0','2018-05-05 13:14:00','There are no rules in this group! Just try to have some fun','http://www.joshuaratliffphotography.com/wp-content/uploads/2018/04/image_20180413_093036.jpg','Rules',23373),(23439,0,'\0','2018-02-12 13:01:00','I love you','https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Love_Heart_SVG.svg/2000px-Love_Heart_SVG.svg.png','Surprise!',23374),(23440,0,'','2018-02-13 23:00:00','WATCH FREE PORN VIDEOS RIGHT HERE: https://www.youtube.com/watch?v=3tmd-ClpJxA','https://cdn.vectorstock.com/i/1000x1000/99/74/free-sex-rubber-stamp-vector-12979974.jpg','FREE SEX',23380),(23441,0,'','2018-02-13 23:30:00','Try our brand new viagra! You can buy it here: https://www.youtube.com/watch?v=J0DjcsK_-HY','http://expresspharmacy.net/wp-content/uploads/2013/11/viagra.jpg','Problems at Sex?',23380),(23442,0,'','2018-03-02 11:00:00','FREE SEX JUST CLICKING HERE: https://www.youtube.com/watch?v=3-NTv0CdFCk','http://www.mytinyphone.com/uploads/users/docfle743/515579.gif','TIRED OF WATCHING PORN?',23384),(23443,0,'','2018-03-02 12:00:00','Too good that now we have CIALIS, buy it here: https://www.youtube.com/watch?v=qfAqtFuGjWM','http://www.gifmania.com/Gif-Animados-Objetos/Imagenes-Ciencia/Medicina/Viagra/Pastillas-De-Viagra-65809.gif','VIAGRA COULD KILL YOU',23384);
/*!40000 ALTER TABLE `announcement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artwork`
--

DROP TABLE IF EXISTS `artwork`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artwork` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `isFinal` bit(1) NOT NULL,
  `isHighlight` bit(1) NOT NULL,
  `photograph` varchar(255) DEFAULT NULL,
  `remark` text,
  `title` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `exhibition_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ilkg9n1afaald54n8fefs1xeq` (`exhibition_id`),
  CONSTRAINT `FK_ilkg9n1afaald54n8fefs1xeq` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artwork`
--

LOCK TABLES `artwork` WRITE;
/*!40000 ALTER TABLE `artwork` DISABLE KEYS */;
INSERT INTO `artwork` VALUES (23335,0,'Vicent Van Gogh','','','https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg/1280px-Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg','The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition of an idealized village. It has been in the permanent collection of the Museum of Modern Art in New York City since 1941, acquired through the Lillie P. Bliss Bequest. Regarded as among Van Gogh\'s finest works, The Starry Night is one of the most recognized paintings in the history of Western culture.','The Starry Night',1889,23334),(23336,0,'Vicent Van Gogh','','','https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Vincent_Willem_van_Gogh_127.jpg/300px-Vincent_Willem_van_Gogh_127.jpg','Sunflowers (original title, in French: Tournesols) is the name of two series of still life paintings by the Dutch painter Vincent van Gogh. The first series, executed in Paris in 1887, depicts the flowers lying on the ground, while the second set, executed a year later in Arles, shows a bouquet of sunflowers in a vase. In the artist\'s mind both sets were linked by the name of his friend Paul Gauguin, who acquired two of the Paris versions','Sunflowers (Van Gogh series)',1880,23334),(23337,0,'Vicent Van Gogh','','','https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Vincent_van_Gogh_-_De_slaapkamer_-_Google_Art_Project.jpg/300px-Vincent_van_Gogh_-_De_slaapkamer_-_Google_Art_Project.jpg','Bedroom in Arles (French: La Chambre à Arles; Dutch: Slaapkamer te Arles) is the title given to each of three similar paintings by 19th-century Dutch Post-Impressionist painter Vincent van Gogh.  Van Gogh\'s own title for this composition was simply The Bedroom (French: La Chambre à coucher). There are three authentic versions described in his letters, easily discernible from one another by the pictures on the wall to the right.','Bedroom in Arles',1888,23334),(23338,0,'Vicent Van Gogh','','','https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Vincent_van_Gogh_-_Almond_blossom_-_Google_Art_Project.jpg/300px-Vincent_van_Gogh_-_Almond_blossom_-_Google_Art_Project.jpg','Almond Blossoms is from a group of several paintings made in 1888 and 1890 by Vincent van Gogh in Arles and Saint-Rémy, southern France of blossoming almond trees. Flowering trees were special to van Gogh. They represented awakening and hope. He enjoyed them aesthetically and found joy in painting flowering trees. The works reflect the influence of Impressionism, Divisionism, and Japanese woodcuts.','Almond Blossoms',1890,23334),(23339,0,'Vicent Van Gogh','','','https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Irises-Vincent_van_Gogh.jpg/300px-Irises-Vincent_van_Gogh.jpg','Irises is one of several paintings of irises by the Dutch artist Vincent van Gogh, and one of a series of paintings he executed at the Saint Paul-de-Mausole asylum in Saint-Rémy-de-Provence, France, in the last year before his death in 1890.','Irises',1889,23334),(23340,0,'Vicent Van Gogh','','','https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Vincent_van_Gogh_-_Self-Portrait_-_Google_Art_Project.jpg/300px-Vincent_van_Gogh_-_Self-Portrait_-_Google_Art_Project.jpg','Self portrait is an 1889 oil on canvas painting by the post-impressionist artist Vincent van Gogh. The picture, which may have been van Gogh\'s last self-portrait, was painted in September that year, shortly before he left Saint-Rémy-de-Provence in southern France.','Van Gogh self-portrait',1889,23334),(23341,0,'Vicent Van Gogh','','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Vincent_van_Gogh_-_The_yellow_house_%28%27The_street%27%29.jpg/300px-Vincent_van_Gogh_-_The_yellow_house_%28%27The_street%27%29.jpg','The Yellow House (Dutch: Het gele huis), alternatively named The Street (Dutch: De straat), is an 1888 oil painting by the 19th-century Dutch Post-Impressionist painter Vincent van Gogh.  The house was the right wing of 2 Place Lamartine, Arles, France, where, on May 1, 1888, Van Gogh rented four rooms. He occupied two large ones on the ground floor to serve as an atelier (workshop) and kitchen, and on the first floor, two smaller ones facing Place Lamartine. The window on the first floor near the corner with both shutters open is that of Van Gogh\'s guest room, where Paul Gauguin lived for nine weeks from late October 1888. Behind the next window, with one shutter closed, is Van Gogh\'s bedroom. The two small rooms at the rear were rented by Van Gogh at a later time.','The Yellow House',1888,23334),(23342,0,'Vicent Van Gogh','','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Van-willem-vincent-gogh-die-kartoffelesser-03850.jpg/300px-Van-willem-vincent-gogh-die-kartoffelesser-03850.jpg','The Potato Eaters (Dutch: De Aardappeleters) is an oil painting by Dutch artist Vincent van Gogh painted in April 1885 in Nuenen, Netherlands. It is in the Van Gogh Museum in Amsterdam. A preliminary oil sketch of the painting is at the Kröller-Müller Museum in Otterlo, and he also made lithographs of the image, which are held in collections including the Museum of Modern Art in New York City. The painting is considered to be one of Van Gogh\'s masterpieces.','The Potato Eaters',1885,23334),(23343,0,'Vicent Van Gogh','','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Vincent_Van_Gogh_-_Wheatfield_with_Crows.jpg/300px-Vincent_Van_Gogh_-_Wheatfield_with_Crows.jpg','Wheatfield with Crows is a July 1890 painting by Vincent van Gogh. It has been cited by several critics as one of his greatest works.  It is commonly stated that this was van Gogh\'s final painting. However, art historians are uncertain as to which painting was van Gogh\'s last, as no clear historical records exist. The evidence of his letters suggests that Wheatfield with Crows was completed around 10 July and predates such paintings as Auvers Town Hall on 14 July 1890 and Daubigny\'s Garden. Moreover, Jan Hulsker points out that a painting of harvested wheat, Field with Stacks of Wheat (F771), must be a later painting.','Wheatfield with Crows',1890,23334),(23344,0,'Vicent Van Gogh','','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Red_vineyards.jpg/300px-Red_vineyards.jpg','The Red Vineyards near Arles is an oil painting by the Dutch painter Vincent van Gogh, executed on a privately primed Toile de 30 piece of burlap in early November 1888. The painting is believed to be the only van Gogh painting ever sold in his lifetime and has been listed among his major works.','The Red Vineyard',1888,23334),(23345,0,'Vicent Van Gogh','','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Van_Gogh_-_Terrasse_des_Caf%C3%A9s_an_der_Place_du_Forum_in_Arles_am_Abend1.jpeg/300px-Van_Gogh_-_Terrasse_des_Caf%C3%A9s_an_der_Place_du_Forum_in_Arles_am_Abend1.jpeg','Café Terrace at Night is a 1888 oil painting by the Dutch artist Vincent van Gogh in 1888. It is also known as The Cafe Terrace on the Place du Forum, and, when first exhibited in 1891, was entitled Coffeehouse, in the evening (Café, le soir).  Van Gogh painted Café Terrace at Night in Arles, France, in mid-September 1888. The painting is not signed, but described and mentioned by the artist in three letters.','Café Terrace at Night',1888,23334),(23346,0,'Vicent Van Gogh','','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Starry_Night_Over_the_Rhone.jpg/300px-Starry_Night_Over_the_Rhone.jpg','Starry Night Over the Rhône (September 1888, French: Nuit étoilée sur le Rhône) is one of Vincent van Gogh\'s paintings of Arles at nighttime. It was painted at a spot on the bank of the Rhône that was only a one or two-minute walk from the Yellow House on the Place Lamartine which Van Gogh was renting at the time. The night sky and the effects of light at night provided the subject for some of his more famous paintings, including Cafe Terrace at Night (painted earlier the same month) and the later canvas from Saint-Rémy, The Starry Night.','Starry Night Over the Rhône',1888,23334),(23348,0,NULL,'','','https://www.out.com/sites/out.com/files/2017/06/24/sasha_velour.jpg','Drag Queen, Illustrator, Graphic Design. Sasha Velour is a Queer Artist living in Brooklyn, NY.','Sasha Velour',NULL,23347),(23350,0,'Peter Paul Rubens','','','https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Peter_Paul_Rubens_-_The_Fall_of_Phaeton_%28National_Gallery_of_Art%29.jpg/300px-Peter_Paul_Rubens_-_The_Fall_of_Phaeton_%28National_Gallery_of_Art%29.jpg','The Fall of Phaeton is a painting by the Flemish master Peter Paul Rubens, featuring the ancient Greek myth of Phaeton (Phaethon), a recurring theme in visual arts. Rubens chose to depict the myth at the height of its action, with the thunderbolts hurled by Zeus to the right.','The Fall of Phaeton',1605,23349),(23353,0,'Jacopo Ligozzi','','','https://i.pinimg.com/originals/0b/b7/c8/0bb7c893443e465c7100908a592393d4.jpg','Jacopo Ligozzi (1547–1627) was an Italian painter, illustrator, designer, and miniaturist. His art can be categorized as late-Renaissance and Mannerist styles.','The Allegory of the Redemption',1587,23352),(23355,0,'Peter Paul Rubens','\0','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Las_Meninas%2C_by_Diego_Vel%C3%A1zquez%2C_from_Prado_in_Google_Earth.jpg/360px-Las_Meninas%2C_by_Diego_Vel%C3%A1zquez%2C_from_Prado_in_Google_Earth.jpg','Las Meninas is a 1656 painting in the Museo del Prado in Madrid, by Diego Velázquez, the leading artist of the Spanish Golden Age. Its complex and enigmatic composition raises questions about reality and illusion, and creates an uncertain relationship between the viewer and the figures depicted. Because of these complexities, Las Meninas has been one of the most widely analyzed works in Western painting.','Diego Velázquez',1656,23354),(23358,0,'Weise Construction Company','\0','','https://www.citymetric.com/sites/default/files/styles/large/public/article_body_2015/12/dortmund_christmas_tree.jpg?itok=_MWdjJP7','The tallest tree of all, in Dortmund, Germany, is a prime example of this more creative approach to tall trees. At around 46m or 150 feet tall every year, it is enormous, the King Kong of Christmas trees.','The tallest Christmas Tree',2017,23357),(23360,0,'Israël Silvestre','','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Isra%C3%ABl_Silvestre%2C_Premi%C3%A8re_journ%C3%A9e%2C_1664.jpg/800px-Isra%C3%ABl_Silvestre%2C_Premi%C3%A8re_journ%C3%A9e%2C_1664.jpg','Israel Silvestre (13 August 1621 in Nancy – 11 October 1691 in Paris), called the Younger to distinguish him from his father, was a prolific French draftsman, etcher and print dealer who specialized in topographical views and perspectives of famous buildings.','Première journée',1664,23352),(23362,0,'Eugène Delacroix','\0','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Delacroix_-_La_Mort_de_Sardanapale_%281827%29.jpg/280px-Delacroix_-_La_Mort_de_Sardanapale_%281827%29.jpg','The Death of Sardanapalus (La Mort de Sardanapale) is an oil painting on canvas by Eugène Delacroix, dated 1827. It currently hangs in the Musée du Louvre, Paris. A smaller replica, painted by Delacroix in 1844, is now in the Philadelphia Museum of Art.','The Death of Sardanapalus',1827,23361),(23364,0,'Jeff Koons','\0','','https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/%22Puppy%22_del_Guggenheim_de_Bilbao_01.JPG/1024px-%22Puppy%22_del_Guggenheim_de_Bilbao_01.JPG','TODO','Puppy',1992,23363),(23366,0,NULL,'','\0','https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Lady_Gaga_Toronto_Film_Festival_2017_%28cropped%29.jpg/250px-Lady_Gaga_Toronto_Film_Festival_2017_%28cropped%29.jpg','Stefani Joanne Angelina Germanotta ()born March 28, 1986), known professionally as Lady Gaga, is an American singer, songwriter, and actress. She is known for her unconventionality and provocative work as well as visual experimentation.','Lady Gaga',2018,23365),(23367,0,NULL,'\0','','https://media1.popsugar-assets.com/files/thumbor/1WMoil4C4wE3-FDMwUGIQ9AHe9o/fit-in/1024x1024/filters:format_auto-!!-:strip_icc-!!-/2018/01/28/075/n/1922564/241932035a6e6f6a68c551.57492641_GettyImages-911491980/i/Lana-Del-Rey.jpg','Elizabeth Woolridge Grant (born June 21, 1985), known professionally as Lana Del Rey, is an American singer and songwriter. Her music has been noted by critics for its stylized cinematic quality, its preoccupation with themes of tragic romance, glamour, and melancholia, and its references to pop culture, particularly 1950s and 1960s Americana.','Lana del Rey',2018,23365),(23368,0,NULL,'','','https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Lorde_Brisbane_Nov_2017_%28cropped%29.jpg/220px-Lorde_Brisbane_Nov_2017_%28cropped%29.jpg','Ella Marija Lani Yelich-O\'Connor (born 7 November 1996), known professionally as Lorde (pronounced lord), is a New Zealand singer, songwriter, and record producer who holds both New Zealand and Croatian citizenship. Born in the Auckland suburb of Takapuna and raised in neighbouring Devonport, she became interested in performing as a child. In her early teens, she signed with Universal Music Group and was later paired with songwriter and record producer Joel Little.','Lorde',2018,23365),(23371,0,NULL,'','','https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/TUT-Ausstellung_FFM_2012_47_%287117819557%29.jpg/245px-TUT-Ausstellung_FFM_2012_47_%287117819557%29.jpg','Tutankhamun was an Egyptian pharaoh of the 18th dynasty (ruled c. 1332–1323 BC in the conventional chronology), during the period of Egyptian history known as the New Kingdom or sometimes the New Empire Period','Tutankamon',NULL,23370);
/*!40000 ALTER TABLE `artwork` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parentCategory_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_3v34vcvwua46xp9jd0bj7rk78` (`parentCategory_id`),
  CONSTRAINT `FK_3v34vcvwua46xp9jd0bj7rk78` FOREIGN KEY (`parentCategory_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (23170,0,'CATEGORY',NULL),(23171,0,'Paintings',23170),(23172,0,'Sculptures',23170),(23173,0,'Other Arts',23170),(23174,0,'Contemporaine',23171),(23175,0,'Byzantine Painting',23171),(23176,0,'Renaissance',23171),(23177,0,'Baroque',23171),(23178,0,'Rococos',23171),(23179,0,'Mesopotamia',23172),(23180,0,'Egypt',23172),(23181,0,'Ancient Greece',23172),(23182,0,'Gothic',23172),(23183,0,'Modern Arts',23173),(23184,0,'Pop History',23183),(23185,0,'Abstract Art',23183),(23186,0,'Modern Style',23183),(23187,0,'Postmodern Era',23183),(23188,0,'Avant-Grade',23183),(23189,0,'Postimpressionisme',23171);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `containsTaboo` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `group_id` int(11) NOT NULL,
  `parentComment_id` int(11) DEFAULT NULL,
  `visitor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_q8dtm49uxgwbbmkhfkuwko4de` (`group_id`),
  KEY `FK_t4l8ri4kvik847hh7ei7y1l4u` (`parentComment_id`),
  KEY `FK_ofqf2f5j7kgoegs0h5mobkja2` (`visitor_id`),
  CONSTRAINT `FK_ofqf2f5j7kgoegs0h5mobkja2` FOREIGN KEY (`visitor_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_q8dtm49uxgwbbmkhfkuwko4de` FOREIGN KEY (`group_id`) REFERENCES `aggroup` (`id`),
  CONSTRAINT `FK_t4l8ri4kvik847hh7ei7y1l4u` FOREIGN KEY (`parentComment_id`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (23444,0,'\0','Nice to meet you guys!','https://media.tenor.com/images/f35aae57f3700459808808ae37886b62/tenor.gif','Hey!',23372,NULL,23244),(23445,0,'\0','Hello hello hello! Can\'t wait to visit the museum!',NULL,'Hello',23372,NULL,23245),(23446,0,'\0','Do you know guys if it will be sunny or rainy? Thank you',NULL,'Important question',23372,NULL,23244),(23447,0,'\0','I don\'t have much money so I need to know that as soon as possible',NULL,'Are the restaurants of the museum cheap?',23372,NULL,23245),(23448,0,'','Is that allowed?',NULL,'I always carry a box of viagra in my bag',23372,NULL,23244),(23449,0,'','I need to have sex with someone',NULL,'Help',23372,NULL,23244),(23450,0,'\0','You\'re so gross',NULL,'Ew',23372,23449,23245),(23451,0,'\0','Wrong chat! Sorry',NULL,'Oops',23372,23450,23244),(23452,0,'\0','Why is everybody so quiet',NULL,'Hello?',23373,NULL,23246),(23453,0,'\0','THIS IS SO CUTE','https://media1.tenor.com/images/4f9e31e2a854c2435d128defd1a1988d/tenor.gif','OMGGG',23374,NULL,23244),(23454,0,'','HAVE SOME FUN WITH BOYS AND GIRLS HERE: https://www.youtube.com/watch?v=Rfj2SUnhYCk',NULL,'FREE SEX',23380,NULL,23247),(23455,0,'','But none of us need free sex',NULL,'Thank you',23380,23454,23248),(23456,0,'','IT\'S GOOD FOR YOUR SOUL',NULL,'BUY VIAGRA',23384,NULL,23247),(23457,0,'','I\'m just kidding hehe',NULL,'I need to buy some cialis',23377,NULL,23246);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `critic`
--

DROP TABLE IF EXISTS `critic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `critic` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_rq8eilj98ork1yxlo7xggp5c8` (`userAccount_id`),
  CONSTRAINT `FK_rq8eilj98ork1yxlo7xggp5c8` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `critic`
--

LOCK TABLES `critic` WRITE;
/*!40000 ALTER TABLE `critic` DISABLE KEYS */;
INSERT INTO `critic` VALUES (23273,0,'4315 Honeysuckle Lane','rubi@gmail.com','FEMALE','Rubí','+34606383526','Koon',23129),(23274,0,NULL,'quique@gmail.com','MALE','Quique','+34348337243','Perkins',23130),(23275,0,'2097 Aviation Way','norberto@gmail.com','MALE','Norberto','+34818218338','Sanchez',23131),(23276,0,'1466 Anmoore Road','minina@gmail.com','FEMALE','Minina','+34347988810','Juarez',23132),(23277,0,'3360 Fire Access Red','belinda@gmail.com','FEMALE','Belinda','+34336887990','Obrien',23153),(23278,0,'2960 Roosevelt Street','proton@gmail.com','MALE','Protón','+34655190700','Walker',23154);
/*!40000 ALTER TABLE `critic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `critique`
--

DROP TABLE IF EXISTS `critique`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `critique` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `description` text,
  `score` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `critic_id` int(11) NOT NULL,
  `exhibition_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m9k088l5lry20wnu8t83h8yh7` (`critic_id`),
  KEY `FK_et93j0955yeipr8b4bb2jernu` (`exhibition_id`),
  CONSTRAINT `FK_et93j0955yeipr8b4bb2jernu` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`),
  CONSTRAINT `FK_m9k088l5lry20wnu8t83h8yh7` FOREIGN KEY (`critic_id`) REFERENCES `critic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `critique`
--

LOCK TABLES `critique` WRITE;
/*!40000 ALTER TABLE `critique` DISABLE KEYS */;
INSERT INTO `critique` VALUES (23458,0,'2018-05-08 22:28:00','My friend and I both loved this exhibition. A combination of paintings, quotes, music, history and van Gogh\'s letters made this a very informative and atmospheric display. Brilliant way to spend a couple of hours. Go and enjoy it.',5,'Brilliant exhibition',23273,23334),(23459,0,'2018-05-09 12:46:00','Superb exhibition, using different methods of engagement with the art work. The music, sound effects, visuals, and story boards were magnificent. We wandered for one and a half hours. Casual seating available to observe and listen. Only negative, someone was behind a screen, banging and building or dismantling something which detracted from the experience.',4,'A must see if in Krakow, highly recommended',23274,23334),(23460,0,'2018-05-11 13:50:00','It was interesting and original from one hand, but exhibition is little bit limited and the main place are showing the same. To see one time but not more',3,'Why not',23275,23334),(23461,0,'2018-05-07 19:38:00','Van Gogh Alive is a very well made, informative and emotional multimedia show featuring the works of this extraordinary artist. The images, the music and the storyline are beautifully aligned. The whole exhibition is spread across five large rooms, so there is not a lot of walking. There are benches and armchairs across the place for the guests to sit and savour.',5,'Very well done!',23276,23334),(23462,0,'2018-05-10 20:56:00','The place is OK for the canvas of the exhibition, although the entrance and the facilities need some serious re-thinking and re-doing for the future exhibitions. If you bring younger children (below 10 I would say), they may get bored, watching art (even if beautifully moving) for an hour straight may be too much of a challenge.',4,'Nice exhibition',23277,23334),(23463,0,'2018-05-11 10:45:00','We had a great time. I relaxed watching on a comfortable chair stunning image projections. But after the show we bought two mugs with images of Van Gog in the gift shop. Unfortunately, both are defective. At home when We poured hot water into the mugs, mugs immediately broke. The next day, the saleswoman refused us to replace the defective cups.',3,'Great exhibition with terrible souvenir shop',23278,23334),(23464,0,'2018-05-14 09:35:00','Sasha Velour may not quite have cracked the British mass public yet the way Courtney was able to in Big Brother, however she certainly conquered my heart when I went to watch her performance hosted by Klub Kids UK at Heaven Liverpool, at the end of January. Although a nightclub wouldn’t have been my first choice to watch a performance that expertly combined the high camp and theatrics of a drag show and the wild passion of a political rally; Sasha told us it reminded her of the tiny clubs of Brooklyn, New York, where she hails from and it felt like a really special moment for both her and the crowd.',5,'Outstanding performance',23273,23347),(23465,0,'2018-05-11 18:59:00','Close up, painted faces decompose into an indecipherable mess of dots and dashes, applied by flicks of a brush. Much the same happens when you approach actual people: from a distance they possess the legible lineaments we call character but, once you begin to analyse their actions and speculate about their motives, they lose focus and blur into complexity and confusion.',4,'As good as expected',23273,23354),(23466,0,'2018-05-09 19:13:00','Her expression is ambiguous, the features half cast in shadow, catching our eye but blurred by a dim light, by the old mirror and by all the soft, veiled edges in the picture. It’s the stripper principle: show less, leave more to the imagination. There isn’t a sexier image in art. Perfect beauty, Velázquez implies, eludes strict definition.',5,'Velázquez, Without Bells or Whistles',23274,23354),(23467,0,'2018-05-13 16:40:00','For a short-sighted son of a Paris policeman, Auguste Rodin went on to have a heck of a career – as one of the greatest sculptors of all time. He’s associated with bold realism, translating into art whatever he saw before him and rejecting the polished, idealised figures of academic sculpture. A new exhibition at the British Museum, however, puts a different slant on his genius. It highlights the influence on Rodin of Ancient Greek art, especially the sculptures from the Parthenon temple in Athens.',4,'An interesting new exhibition',23273,23356),(23468,0,'2018-05-12 19:22:00','If ever there were an exhibition that plays to a museum’s strengths, it’s this one. Rodin and the Art of Ancient Greece is about the artist’s obsession with Greek statuary in general and the Elgin Marbles in particular. So the exhibition is a kind of dialogue between Rodin and the artefacts of the museum, which he first visited in 1881 and loved. ',3,'Rodin and the Art of Ancient Greece',23276,23356),(23469,0,'2017-12-20 17:49:00','I took a midday, guided tour of the Vatican Museums, Sistine Chapel and St. Peter\'s Basilica. All three are breathtakingly beautiful. Booking a guided tour was the best decision I made as it allowed us to skip the lines. Heavens...the lines! If I\'d attempted to navigate the Vatican Museums on my own, I\'d still be there right now. Be prepared to do a lot of walking regardless of whether you do a guided tour or self guided tour. Also, the dress code (arms and shoulders must be covered) is enforced. I was stopped by a guard as my group entered St. Peter\'s Basilica. My shoulders were covered, but my arms were not because my shirt was sleeveless (it was 80 degrees). Luckily for me, my tour guide carried an extra scarf and I was able to cover my arms. ',4,'Breathtaking with big crowds!',23273,23357),(23470,0,'2018-05-12 16:33:00','The French artist’s more mature work offers a broad perspective of Paris. The artworks depict royal festivities and changes that the city underwent coupled with the outlines of the cities conquered by Louis XIV in Lorraine and the Ardennes. Silvestre’s series dedicated to the famous châteaux of the Île-de-France —Vaux-le-Vicomte, Meudon, Montmorency, and Versailles among them — also displayed his talent for capturing architecture.',3,'France Viewed from the Grand Siècle',23273,23359),(23471,0,'2018-05-13 19:21:00','The tempestuous paintings of Delacroix are at odds with a man who was in complete control. You’ll have to search hard to find them, though, in this long overdue show.',4,'Delacroix and the Rise of Modern Art ',23277,23361),(23472,0,'2018-05-01 22:11:00','A really fun place to see, great variety of exhibits to see within from celebrities, royals, animation, something for everyone. The ride towards the end of the tour was most enjoyable as well, well worth the money.',5,'A must see attraction if in London',23278,23365),(23473,0,'2017-12-19 22:49:00','The skip the line with a time works (but not really needed. REMEMBER to cover shoulders and knees. The audio guide is hard to follow and as the tour is so long I just gave up. Some of the items and areas are amazing others you can just speed through. It’s worth a visit (once).',4,'Too much to see',23277,23357);
/*!40000 ALTER TABLE `critique` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daypass`
--

DROP TABLE IF EXISTS `daypass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `daypass` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `VAT` double DEFAULT NULL,
  `CVV` int(11) DEFAULT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `purchaseMoment` datetime DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `visitDate` datetime DEFAULT NULL,
  `exhibition_id` int(11) DEFAULT NULL,
  `museum_id` int(11) NOT NULL,
  `visitor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9xxn4eysa7pxw8pkyqc20dxcm` (`exhibition_id`),
  KEY `FK_ky5jb9g5p0lcoer9dgn2vpqs0` (`museum_id`),
  KEY `FK_8adrwf6gt8b1k5hep50cr57sf` (`visitor_id`),
  CONSTRAINT `FK_8adrwf6gt8b1k5hep50cr57sf` FOREIGN KEY (`visitor_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_9xxn4eysa7pxw8pkyqc20dxcm` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`),
  CONSTRAINT `FK_ky5jb9g5p0lcoer9dgn2vpqs0` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daypass`
--

LOCK TABLES `daypass` WRITE;
/*!40000 ALTER TABLE `daypass` DISABLE KEYS */;
INSERT INTO `daypass` VALUES (23310,1,0.21,576,'VISA','Tricia',10,'4635876275165858',2020,16.94,'2018-05-05 23:51:00','MDP-visitor1-0005','2018-05-13 00:00:00',23334,23206,23243),(23311,1,0.22,576,'VISA','Tricia',10,'4635876275165858',2020,19.52,'2018-02-08 16:45:00','MDP-visitor1-0002','2018-02-14 00:00:00',23349,23206,23243),(23312,0,0.25,576,'VISA','Tricia',10,'4635876275165858',2020,80,'2018-01-28 13:55:00','TBM-visitor1-0000','2018-03-02 00:00:00',NULL,23213,23243),(23313,1,0.21,576,'VISA','Tricia',10,'4635876275165858',2020,122.19,'2017-12-24 21:23:00','TVM-visitor1-0000','2018-01-05 00:00:00',23357,23218,23243),(23314,1,0.21,576,'VISA','Tricia',10,'4635876275165858',2020,8.45,'2018-03-02 21:29:00','EGEC-visitor1-0000','2018-03-25 00:00:00',23370,23239,23243),(23315,1,0.2,879,'Master Card','Surfín',12,'4874723107648372',2022,19.2,'2018-02-09 08:21:00','MDP-visitor2-0003','2018-02-13 00:00:00',23349,23206,23244),(23316,0,0.18,879,'Master Card','Surfín',12,'4874723107648372',2022,80,'2018-01-30 10:58:00','TBM-visitor2-0002','2018-03-02 00:00:00',NULL,23213,23244),(23317,0,0.2,879,'Master Card','Surfín',12,'4874723107648372',2022,67.9,'2018-04-13 12:40:00','TL-visitor2-0003','2018-04-14 00:00:00',NULL,23220,23244),(23318,1,0.21,879,'Master Card','Surfín',12,'4874723107648372',2022,8.45,'2018-03-02 21:31:00','EGEC-visitor2-0001','2018-03-25 00:00:00',23370,23239,23244),(23319,1,0.21,110,'Master Card','Bruno',3,'5408480764266038',2019,19.36,'2018-02-02 09:11:00','MDP-visitor6-0001','2018-02-11 00:00:00',23349,23206,23248),(23320,0,0.27,110,'Master Card','Bruno',3,'5408480764266038',2019,80,'2018-05-06 12:58:00','TBM-visitor6-0003','2018-05-13 00:00:00',NULL,23213,23248),(23321,1,0.25,110,'Master Card','Bruno',3,'5408480764266038',2019,126.23,'2017-12-29 15:29:00','TVM-visitor6-0001','2017-12-30 00:00:00',23357,23218,23248),(23322,1,0.25,462,'VISA','Martita',4,'5138023518721010',2020,20,'2018-02-01 18:11:00','MDP-visitor4-0000','2018-02-14 00:00:00',23349,23206,23246),(23323,1,0.25,462,'VISA','Martita',4,'5138023518721010',2020,37.5,'2018-05-06 19:44:00','MT-visitor4-0001','2018-05-13 00:00:00',23365,23230,23246),(23324,1,0.21,483,'VISA','Arándano',11,'5102532708147812',2025,19.36,'2018-02-11 11:56:00','MDP-visitor5-0004','2018-02-13 00:00:00',23349,23206,23247),(23325,0,0.2,483,'VISA','Arándano',11,'5102532708147812',2025,80,'2018-01-28 19:02:00','TBM-visitor5-0001','2018-02-01 00:00:00',NULL,23213,23247),(23326,0,0.29,483,'VISA','Arándano',11,'5102532708147812',2025,67.9,'2018-02-09 20:28:00','TL-visitor5-0000','2018-02-14 00:00:00',NULL,23220,23247),(23327,1,0.31,483,'VISA','Arándano',11,'5102532708147812',2025,39.3,'2018-05-04 10:19:00','MT-visitor5-0000','2018-05-13 00:00:00',23365,23230,23247),(23328,1,0.3,576,'VISA','Tricia',10,'4635876275165858',2020,14.3,'2018-05-14 01:37:00','MDP-visitor1-0006','2018-08-21 00:00:00',23352,23206,23243),(23329,1,0.21,879,'Master Card','Surfín',12,'4874723107648372',2022,55.66,'2018-05-14 00:31:00','MDP-visitor2-0007','2018-05-27 00:00:00',23354,23206,23244),(23330,1,0.21,462,'VISA','Martita',4,'5138023518721010',2020,12.21,'2018-04-12 19:21:00','TL-visitor4-0002','2018-07-15 00:00:00',23359,23220,23246),(23331,1,0.24,483,'VISA','Arándano',11,'5102532708147812',2025,21.93,'2018-03-03 04:34:00','TL-visitor5-0001','2018-07-19 00:00:00',23361,23220,23247),(23332,1,0.28,110,'Master Card','Bruno',3,'5408480764266038',2019,24.32,'2018-04-14 17:56:00','GM-visitor6-0000','2018-08-03 00:00:00',23363,23228,23248),(23333,0,0.22,110,'Master Card','Bruno',3,'5408480764266038',2019,49.99,'2018-05-05 18:23:00','TNG-visitor6-0000','2018-06-16 00:00:00',NULL,23235,23248);
/*!40000 ALTER TABLE `daypass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `director`
--

DROP TABLE IF EXISTS `director`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `director` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lyi14cqa3xj6jq8qtoe4txxmi` (`userAccount_id`),
  CONSTRAINT `FK_lyi14cqa3xj6jq8qtoe4txxmi` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `director`
--

LOCK TABLES `director` WRITE;
/*!40000 ALTER TABLE `director` DISABLE KEYS */;
INSERT INTO `director` VALUES (23190,0,'3062 Tully Street','pili@gmail.com','FEMALE','Pili','+34907200114','Beaton',23137),(23191,0,'2052 Lyon Avenue','estela@gmail.com','FEMALE','Estela','+34443226248','Sharma',23138),(23192,0,'109 Marshville Road','totakk@gmail.com','MALE','Tota KK','+34615710397','Richardson',23139),(23193,0,'3436 Goff Avenue','elio@gmail.com','MALE','Elio','+34484368739','Petit',23140),(23194,0,NULL,'tomNook@gmail.com','FEMALE','Tom','+34907200114','Nook',23141),(23195,0,NULL,'socrates@gmail.com','MALE','Sócrates','+348032351835','Alvarado',23142);
/*!40000 ALTER TABLE `director` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exhibition`
--

DROP TABLE IF EXISTS `exhibition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exhibition` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` text,
  `endingDate` datetime DEFAULT NULL,
  `isPrivate` bit(1) NOT NULL,
  `price` double DEFAULT NULL,
  `startingDate` datetime DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8iibj36b9i2vnlg3f8dlswvy8` (`ticker`),
  KEY `FK_qbey1br2ew2c8mn3oijob26me` (`category_id`),
  KEY `FK_j8tp9kwexl25qg3oejuukotj` (`room_id`),
  CONSTRAINT `FK_j8tp9kwexl25qg3oejuukotj` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FK_qbey1br2ew2c8mn3oijob26me` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exhibition`
--

LOCK TABLES `exhibition` WRITE;
/*!40000 ALTER TABLE `exhibition` DISABLE KEYS */;
INSERT INTO `exhibition` VALUES (23334,0,'Van Gogh Alive is just the kind of inspiring, imagination-expanding experience we\'re known for delivering to visitors','2018-07-13 21:00:00','',13,'2018-05-13 10:00:00','director1-VanGA','Van Gogh Alive',23189,23207),(23347,0,'Nightgowns is a monthly live theatrical showcase dedicated to exceptional drag performance of all kinds! First created at Bizarre Bushwick, the show has now taken residency at National Sawdust in Williamsburg','2018-07-20 23:59:00','\0',0,'2018-07-14 20:00:00','director1-SashaVelourNG','Sasha Velour: NIGHTGOWNS',23185,23207),(23349,0,'The Museo del Prado and the Boijmans Van Beuningen Museum are presenting the exhibition Rubens. Painter of Sketches. Sponsored by Fundación AXA and with the collaboration of the Government of Flanders, it offers an analysis of Rubens as the most important painter of oil sketches in the history of European art.','2018-02-15 22:00:00','',15,'2018-02-10 12:00:00','director1-RubensPS','Rubens. Painter of Sketches',23177,23208),(23351,0,'The exhibition brings together a selection of nine paintings on monochrome stone (slate and white marble) by Italian painters such as Sebastiano del Piombo, Titian, Daniele da Volterra and Leandro Bassano.','2018-08-18 20:00:00','\0',0,'2018-08-08 10:00:00','director1-InLapDep','In Lapide Depictum',23177,23209),(23352,0,'The outstanding quality of the Óscar Alzaga Villaamil donation makes it a significant addition to the Museo Nacional del Prado’s collection.','2018-08-23 23:00:00','',10,'2018-08-20 20:00:00','director1-TheOAD','The Óscar Alzaga Donation',23177,23209),(23354,0,'Comprising 61 thematically organised paintings, the exhibition aims to offer Japanese visitors an exceptional opportunity to appreciate the art of Velázquez and to understand it in relation to the art of his Spanish and European contemporaries','2019-05-29 17:00:00','',45,'2018-05-20 12:00:00','director1-VLZQZ','Velázquez and the Celebration of Painting: the Golden Age in the Museo del Prado',23177,23210),(23356,0,'This exhibition focuses on the friendship of the artists Niko Ghika and John Craxton, and the writer Patrick Leigh Fermor. Their shared love of Greece was fundamental to their work, as they embraced its sights, sounds, colours and people.','2018-07-20 19:00:00','\0',0,'2018-07-10 10:00:00','director1-Greece','Charmed lives in Greece',23181,23214),(23357,0,'The Museum continues a longstanding holiday tradition with the presentation of its Christmas tree, a favorite of Vaticans and visitors from around the world','2018-01-06 21:00:00','',99.99,'2017-12-24 09:00:00','director1-Christmas','Christmas Tree and Neapolitan Baroque Crèche',23177,23219),(23359,0,'While Silvestre\'s engravings circulated widely, his drawings remain relatively unknown. The Musée du Louvre is home to a remarkable collection of them, to be shown to the public for the first time.','2018-07-24 22:30:00','',10,'2018-07-14 10:00:00','director1-IsrSilv','France Viewed from the Grand Siècle Drawings by Israël Silvestre',23174,23221),(23361,0,'Eugène Delacroix was one of the giants of French painting, but his last full retrospective exhibition in Paris dates back to 1963, the centenary year of his death. In collaboration with the Metropolitan Museum of Art in New York, the Louvre is holding a historic exhibition featuring some 180 works—mostly paintings—as a tribute to his entire career.','2018-07-22 15:00:00','',16.69,'2018-07-18 15:00:00','director1-Delacroix','Delacroix',23177,23222),(23363,0,'Art and China after 1989 presents work by some sixty key artists and groups active across China and worldwide whose critical provocations aim to forge reality free from ideology, to establish the individual apart from the collective, and to define contemporary Chinese experience in universal terms.','2018-08-20 20:00:00','',18,'2018-07-20 10:00:00','director2-ArtNChina','Art and China after 1989: Theater of the World',23187,23229),(23365,0,'From legendary icons such as Madonna and Diana Ross, to iconic legends of today as Lady Gaga or Lana Del Rey. This month, in Madame Tussauds, is dedicated to those girls who made an impact in pop culture.','2018-07-13 22:00:00','',29,'2018-05-13 10:00:00','director4-HERStoryPopStars','HERStory of the World: Pop Stars',23184,23231),(23369,0,'One exhibition featuring Monet’s paintings and nothing else? That’s a rare thing. The last time the UK hosted a Monet exhibition like this was almost 20 years ago. But \'Monet and Architecture\' is not just rare, it’s never been done before.','2018-10-29 22:30:00','',80.99,'2018-10-01 08:00:00','director5-MonetNArch','The Credit Suisse Exhibition: Monet and Architecture',23189,23236),(23370,0,'BATIMAT EGYPT will offer the complete range of products and solutions suited to the construction market in Egypt and the Middle East.','2018-12-31 23:00:00','',5.99,'2018-08-10 08:00:00','director6-BatimatEgypt','BATIMAT EGYPT 2018',23180,23240);
/*!40000 ALTER TABLE `exhibition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exhibition_websites`
--

DROP TABLE IF EXISTS `exhibition_websites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exhibition_websites` (
  `Exhibition_id` int(11) NOT NULL,
  `websites` varchar(255) DEFAULT NULL,
  KEY `FK_5m7fjjmxos4vyuuo8uoyx1usb` (`Exhibition_id`),
  CONSTRAINT `FK_5m7fjjmxos4vyuuo8uoyx1usb` FOREIGN KEY (`Exhibition_id`) REFERENCES `exhibition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exhibition_websites`
--

LOCK TABLES `exhibition_websites` WRITE;
/*!40000 ALTER TABLE `exhibition_websites` DISABLE KEYS */;
INSERT INTO `exhibition_websites` VALUES (23334,'https://vangogh.es/es/inicio/'),(23347,'http://sashavelour.com/nightgowns/'),(23349,'https://www.museodelprado.es/en/whats-on/exhibition/rubens-painter-of-sketches/608ff3ed-0823-40a4-bc5a-9a5cb634d8b0\n				'),(23349,'https://www.museodelprado.es/en'),(23351,'https://www.museodelprado.es/en/whats-on/exhibition/in-lapide-depictum/ef8ddd0c-f7c0-4e75-ba54-22b5af409ec7\n				'),(23352,'https://www.museodelprado.es/en/whats-on/exhibition/the-oscar-alzaga-donation/a01942b7-040b-4247-9193-f254a8002c79\n				'),(23354,'https://www.museodelprado.es/en/whats-on/exhibition/velazquez-and-the-celebration-of-painting-the/78975c56-0137-e685-e5d1-48ea49469812\n				'),(23356,'http://www.britishmuseum.org/whats_on/exhibitions/charmed_lives_in_greece.aspx\n				'),(23357,'https://www.metmuseum.org/exhibitions/listings/2014/christmas-tree\n				'),(23359,'https://www.louvre.fr/en/expositions/france-viewed-grand-siecledrawings-israel-silvestre-1621-1691\n				'),(23361,'https://www.louvre.fr/en/expositions/delacroix-1798-1863\n				'),(23363,'https://arteychina.guggenheim-bilbao.eus/en/exhibition\n				'),(23365,'https://www.madametussauds.com/'),(23369,'https://www.nationalgallery.org.uk/whats-on/exhibitions/the-credit-suisse-exhibition-monet-architecture\n				'),(23370,'https://www.eventseye.com/fairs/f-batimat-egypt-25153-1.html\n				');
/*!40000 ALTER TABLE `exhibition_websites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guide`
--

DROP TABLE IF EXISTS `guide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guide` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ntsbh3t5dd6tw1ovubvnga7ye` (`userAccount_id`),
  CONSTRAINT `FK_ntsbh3t5dd6tw1ovubvnga7ye` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guide`
--

LOCK TABLES `guide` WRITE;
/*!40000 ALTER TABLE `guide` DISABLE KEYS */;
INSERT INTO `guide` VALUES (23196,0,'2649 Skips Lane','pascual@gmail.com','MALE','Pascual','+34928462331','Pruitt',23143),(23197,0,NULL,'ketchup@gmail.com','OTHER','Ketchup','+34337201478','Nations',23144),(23198,0,'2543 Oxford Court','miki@gmail.com','MALE','Miki','+34601953203','Rankin',23145),(23199,0,'4503 Christine Way','marina@gmail.com','FEMALE','Marina','+34978778261','J King',23146),(23200,0,'4205 Spruce Divine','jeremias@gmail.com','MALE','Jeremías','+34412280844','Abbott',23147),(23201,0,'3408 May Street','natura@gmail.com','FEMALE','Natura','+346066260439','Shrader',23148),(23202,0,'3908 Birch Street','camember@gmail.com','MALE','Camember','+34915541233','Hogan',23149),(23203,0,NULL,'vacarena@gmail.com','FEMALE','Vacarena','+34561633002','Corby',23150),(23204,0,'3657 Smith Road','nenufar@gmail.com','FEMALE','Nenufar','+34770894273','Hutchinson',23151),(23205,0,'2649 Skips Lane','luisa@gmail.com','OTHER','Luisa','+34310699097','Mathias',23152);
/*!40000 ALTER TABLE `guide` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guide_exhibition`
--

DROP TABLE IF EXISTS `guide_exhibition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guide_exhibition` (
  `guides_id` int(11) NOT NULL,
  `exhibitions_id` int(11) NOT NULL,
  KEY `FK_euhoglujt5dk5137toi0xa7je` (`exhibitions_id`),
  KEY `FK_4pnr3rc5sw61jkox9dlal9aek` (`guides_id`),
  CONSTRAINT `FK_4pnr3rc5sw61jkox9dlal9aek` FOREIGN KEY (`guides_id`) REFERENCES `guide` (`id`),
  CONSTRAINT `FK_euhoglujt5dk5137toi0xa7je` FOREIGN KEY (`exhibitions_id`) REFERENCES `exhibition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guide_exhibition`
--

LOCK TABLES `guide_exhibition` WRITE;
/*!40000 ALTER TABLE `guide_exhibition` DISABLE KEYS */;
INSERT INTO `guide_exhibition` VALUES (23196,23334),(23196,23351),(23197,23347),(23197,23352),(23197,23357),(23197,23334),(23198,23349),(23198,23354),(23198,23359),(23198,23361),(23198,23334),(23198,23347),(23199,23356),(23199,23334),(23200,23363),(23200,23334),(23201,23370),(23201,23334),(23202,23365),(23203,23357);
/*!40000 ALTER TABLE `guide_exhibition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('DomainEntity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incident`
--

DROP TABLE IF EXISTS `incident`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `incident` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `isChecked` bit(1) NOT NULL,
  `level` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `guide_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1j64uo893d1j4vbuw0i3rd0o8` (`guide_id`),
  KEY `FK_hh3b0qxp9gh8lj1yiomcu5nyn` (`room_id`),
  CONSTRAINT `FK_hh3b0qxp9gh8lj1yiomcu5nyn` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FK_1j64uo893d1j4vbuw0i3rd0o8` FOREIGN KEY (`guide_id`) REFERENCES `guide` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incident`
--

LOCK TABLES `incident` WRITE;
/*!40000 ALTER TABLE `incident` DISABLE KEYS */;
INSERT INTO `incident` VALUES (23386,0,'','MEDIUM','A light bulb is not working',23196,23207),(23387,0,'\0','MEDIUM','A chair disappeared',23196,23215),(23388,0,'','HIGH','A wall seems like is about to fall',23196,23219),(23389,0,'\0','HIGH','One window is broken',23196,23221),(23390,0,'','LOW','The floor is not cleaned',23196,23210),(23391,0,'\0','LOW','Someone forgot his or her bag in the room',23196,23207),(23392,0,'','HIGH','A little child has lost his family',23197,23227),(23393,0,'\0','HIGH','Electricity is not working',23198,23226),(23394,0,'','LOW','Someone started a shooting range',23200,23222),(23395,0,'\0','HIGH','The fans are not working and about to fall',23198,23210),(23396,0,'','MEDIUM','There is a pudde of oil',23199,23209),(23397,0,'\0','LOW','Door\'s hinges need some oil',23197,23225),(23398,0,'\0','HIGH','A wax figure is about to fall',23202,23231),(23399,0,'','HIGH','The fishbowl is about to break',23203,23237),(23400,0,'\0','MEDIUM','A mummy has resurrected',23201,23240);
/*!40000 ALTER TABLE `incident` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitation`
--

DROP TABLE IF EXISTS `invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitation` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `isAccepted` bit(1) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `sentMoment` datetime DEFAULT NULL,
  `group_id` int(11) NOT NULL,
  `guest_id` int(11) NOT NULL,
  `host_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cen7765xfec0yqv28st3p50e7` (`group_id`),
  KEY `FK_epmv4ikdppx6m2h76wlwkca88` (`guest_id`),
  KEY `FK_k9wyr1cu15kwgf4yeeo4n5wl2` (`host_id`),
  CONSTRAINT `FK_k9wyr1cu15kwgf4yeeo4n5wl2` FOREIGN KEY (`host_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_cen7765xfec0yqv28st3p50e7` FOREIGN KEY (`group_id`) REFERENCES `aggroup` (`id`),
  CONSTRAINT `FK_epmv4ikdppx6m2h76wlwkca88` FOREIGN KEY (`guest_id`) REFERENCES `visitor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitation`
--

LOCK TABLES `invitation` WRITE;
/*!40000 ALTER TABLE `invitation` DISABLE KEYS */;
INSERT INTO `invitation` VALUES (23401,0,'','Let\'s go to the British Museum!!','2018-05-05 13:01:00',23373,23243,23244),(23402,0,'','Let\'s go to the British Museum!!','2018-05-05 13:02:00',23373,23248,23244),(23403,0,'','Let\'s go to the British Museum!!','2018-05-05 13:01:00',23373,23247,23244),(23404,0,'','Let\'s go to the British Museum!!','2018-05-05 13:02:00',23373,23246,23244),(23405,0,'','Wanna have a date with me?','2018-02-12 13:11:00',23374,23244,23243),(23406,0,'','let\'s have some fun!!!','2018-04-21 19:51:00',23375,23246,23243),(23407,0,'','let\'s have some fun!!!','2018-04-21 19:52:00',23375,23247,23243),(23408,0,'','Wanna come to El Prado with me?','2018-04-21 20:25:00',23376,23248,23243),(23409,0,'','Accept it please I\'m so bored','2017-12-17 23:02:00',23377,23244,23243),(23410,0,'','Accept it please I\'m so bored','2017-12-17 23:03:00',23377,23246,23243),(23411,0,'','Ha ha ha','2018-03-03 20:31:00',23378,23246,23244),(23412,0,'','Ha ha ha','2018-03-03 20:32:00',23378,23248,23244),(23413,0,'','Surprise!','2018-02-13 21:07:00',23380,23248,23247),(23414,0,'','yayyyy','2018-05-08 11:15:00',23383,23247,23246),(23415,0,NULL,'Ha ha ha','2018-03-03 20:33:00',23378,23243,23244),(23416,0,NULL,'Surprise!','2018-02-13 21:08:00',23380,23243,23247),(23417,0,NULL,'yayyyy','2018-05-08 11:16:00',23383,23243,23246),(23418,0,NULL,'Wanna come to El Prado with me?','2018-04-21 20:26:00',23376,23246,23243),(23419,0,NULL,'Surprise!','2018-02-13 21:07:00',23380,23246,23247),(23420,0,'','Let\'s go to the British Museum!!','2018-05-05 13:15:00',23373,23245,23244);
/*!40000 ALTER TABLE `invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `museum`
--

DROP TABLE IF EXISTS `museum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `museum` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `slogan` varchar(255) DEFAULT NULL,
  `director_id` int(11) NOT NULL,
  `store_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dfopy3k5j29ufc6b8pro6qaji` (`identifier`),
  KEY `FK_139hun7wm4u7u36dkl37tnqlj` (`director_id`),
  KEY `FK_i53dhce858ylhtvn6486l3jvu` (`store_id`),
  CONSTRAINT `FK_i53dhce858ylhtvn6486l3jvu` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`),
  CONSTRAINT `FK_139hun7wm4u7u36dkl37tnqlj` FOREIGN KEY (`director_id`) REFERENCES `director` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `museum`
--

LOCK TABLES `museum` WRITE;
/*!40000 ALTER TABLE `museum` DISABLE KEYS */;
INSERT INTO `museum` VALUES (23206,1,'28014 Paseo del Prado','https://upload.wikimedia.org/wikipedia/commons/9/94/Logo_del_Museo_Nacional_del_Prado.png',40.413889,-3.6925,'padromuseo@gmail.com','MDP','The Prado','+34688912668',58.99,'200 Years sharing art with you',23190,23291),(23213,1,'Great Russell St, Bloomsbury','http://files.websitebuilder.prositehosting.co.uk/fasthosts23063/image/britishmuseumlogo_6515.jpg',51.5183162601,-0.12291117502,'britishmuseum@gmail.com','TBM','The British Museum','+34654112980',80,'© 2017 Trustees of the British Museum',23190,23298),(23217,1,'1000 5th Ave','https://static.dezeen.com/uploads/2016/02/new-metropolitan-art-museum-logo-wolff-olins_dezeen_1568_0.jpg',40.7794162,-73.9633727,'newyorkart@gmail.com','TMMA','The Metropolitan Museum of Art','+34667120556',49.99,'Founded in 1870',23190,23301),(23218,0,'Viale Vaticano, 00165','http://www.museivaticani.va/etc/designs/museivaticani/release/library/main/images/favicon-social-1500.png',41.903829718,12.452664856,'vaticanmuseums@gmail.com','TVM','The Vatican Museums','+34655900879',60.5,'Art, aside from being a credible witness to the beauty of creation, is also a tool of evangelisation',23190,NULL),(23220,1,'Rue de Rivoli, 75001','https://d302e0npexowb4.cloudfront.net/wp-content/uploads/2016/11/Louvre-Logo.jpg',48.86072,2.34096,'thelouvre@gmail.com','TL','The Louvre','+34691122467',67.9,'Préparer sa visite, connaître l\'actualité du musée, participer aux activités, découvrir les œuvres.',23190,23302),(23224,0,'Palace Square, 2, Sankt-Peterburg','https://s-media-cache-ak0.pinimg.com/originals/3c/18/50/3c1850453a810e9edc8f5b68d560578b.png',59.941065,30.317846,'statehermitage@gmail.com','SH','State Hermitage','+34667129433',18.99,'Not Everyone will be Taken into the Future',23190,NULL),(23228,1,'Avenida Abandoibarra, 2 48009','https://d302e0npexowb4.cloudfront.net/wp-content/uploads/2016/11/guggenheim-logo.png',43.268606,-2.934286,'guggenheim@gmail.com','GM','Guggenheim','+34655980600',49.99,'© FMGB Guggenheim Bilbao Museoa, 2018',23191,23304),(23230,1,'Marylebone Rd, Marylebone','http://www.qverlondres.com/wp-content/uploads/2016/10/mdame-tussauds-museo-de-cera-londres.jpg',51.5228,-0.155278,'tussauds@gmail.com','MT','Madame Tussauds','+34899123666',60,'Get closer to the stars in London',23193,23307),(23235,0,'Trafalgar Square, London','http://3.bp.blogspot.com/-vpxgw6XtyDM/TVuxpAMFgnI/AAAAAAAABho/03rIOoY6tsY/s1600/NG_Logo_White.png',51.508704,-0.128435,'nationalgallery@gmail.com','TNG','The National Gallery','+34998234776',1,'The story of European art, masterpiece by masterpiece.',23194,NULL),(23238,0,'Paseo del Prado, 8','https://www.vectorlogo.es/wp-content/uploads/2016/01/logo-vector-museo-thyssen-bornemisza.jpg',40.416111,-3.695,'thyssen@gmail.com','MTB','Museo Thyssen-Bornemisza','+34665900800',35.55,'Un paseo por la historia del arte. Visita obligada en Madrid.',23194,NULL),(23239,1,'15 Meret Basha, Ismailia','https://i.pinimg.com/originals/dc/cc/de/dcccde3f0d9d0794318412768bddf5f2.png',30.047778,31.233333,'elcairomuseum@gmail.com','EGEC','Egyptian Museum','+34677122980',29.99,'Home to an extensive collection of ancient Egyptian antiquities',23195,23309);
/*!40000 ALTER TABLE `museum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `museum_guide`
--

DROP TABLE IF EXISTS `museum_guide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `museum_guide` (
  `Museum_id` int(11) NOT NULL,
  `guides_id` int(11) NOT NULL,
  KEY `FK_fcq5lt2oxtf42ngial6s8cpd0` (`guides_id`),
  KEY `FK_4li5ehoi1f8cosq9qj2walue4` (`Museum_id`),
  CONSTRAINT `FK_4li5ehoi1f8cosq9qj2walue4` FOREIGN KEY (`Museum_id`) REFERENCES `museum` (`id`),
  CONSTRAINT `FK_fcq5lt2oxtf42ngial6s8cpd0` FOREIGN KEY (`guides_id`) REFERENCES `guide` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `museum_guide`
--

LOCK TABLES `museum_guide` WRITE;
/*!40000 ALTER TABLE `museum_guide` DISABLE KEYS */;
INSERT INTO `museum_guide` VALUES (23206,23196),(23206,23197),(23206,23198),(23206,23199),(23206,23200),(23206,23201),(23213,23196),(23213,23198),(23213,23199),(23218,23197),(23218,23199),(23220,23198),(23224,23199),(23224,23196),(23228,23200),(23230,23202),(23235,23203),(23235,23204),(23238,23205),(23239,23201);
/*!40000 ALTER TABLE `museum_guide` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `store_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qn519odsge2mmd5htarooejeu` (`store_id`),
  CONSTRAINT `FK_qn519odsge2mmd5htarooejeu` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (23292,0,'7681234569987','3D Menina. Made from eco-friendly birch plywood.','Red Wooden Menina',50,23291),(23293,0,'3681234569982','Woven in Tunisia on a traditional loom.','Fouta (burgundy)',30,23291),(23294,0,'1684234565932','Case for glasses which reproduces a detail from the triptych The Garden of Earthly Delights (Hieronymus Bosch, 1500-1505). Cleaning cloth included with other detail from the same work.','\'The Garden of Earthly Delights\' Glasses Case',9,23291),(23295,0,'2384536567038','Practical cotton bag with the Museo del Prado logo.','Prado Red Bag',3.95,23291),(23296,0,'8987566567234','T-shirt with the Museo del Prado logo.','Blue Navy Prado T-shirt',20,23291),(23297,0,'1684234565932','Case for glasses which reproduces a detail from the triptych The Garden of Earthly Delights (Hieronymus Bosch, 1500-1505). Cleaning cloth included with other detail from the same work.','\'The Garden of Earthly Delights\' Glasses Case',9,23291),(23299,0,'6571239871241','Created especially for the British Museum, this 4GB USB stick is based on an ancient Greek pot dated to around 540BC-530BC. ','Greek pot USB',14.99,23298),(23300,0,'7682659801233','A ceramic mug featuring the iconic image of the famous Japanese print Fuji Wave by Hokusai. ','Fuji Wave mug',11.99,23298),(23303,0,'7681239803452','53% cotton, 24% nylon, 22% polyester, 2% elastane.','Socks Mona Lisa - Lavender for woman',9.95,23302),(23305,0,'1238794651231','Spiral notebook with 60 blank pages featuring Jeff Koons’, Puppy, 1992, and Guggenheim Bilbao logo on the cover.','GREEN PUPPY NOTEBOOK',6,23304),(23306,0,'6758349801027','Women’s 100% cotton t-shirt with representation of Jeff Koons’ Puppy, designed exclusively for the Guggenheim Museum Bilbao by Basque fashion company Minimil.','PUPPY T-SHIRT',29.7,23304),(23308,0,'5754658291024','Vinyl figure of 9cms','Funko Pop: The Countess',15.8,23307);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_pictures`
--

DROP TABLE IF EXISTS `product_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_pictures` (
  `Product_id` int(11) NOT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  KEY `FK_388qr0bkr4lmxmau40p2f3xpi` (`Product_id`),
  CONSTRAINT `FK_388qr0bkr4lmxmau40p2f3xpi` FOREIGN KEY (`Product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_pictures`
--

LOCK TABLES `product_pictures` WRITE;
/*!40000 ALTER TABLE `product_pictures` DISABLE KEYS */;
INSERT INTO `product_pictures` VALUES (23292,'http://www.tiendaprado.com/7754-large_default/menina-de-madera-roja-grande.jpg\n				'),(23293,'http://www.tiendaprado.com/8645-large_default/fouta-fortuny-burdeos.jpg\n				'),(23293,'http://www.tiendaprado.com/8646-large_default/fouta-fortuny-burdeos.jpg\n				'),(23294,'http://www.tiendaprado.com/6004-large_default/estuche-de-gafas-el-jardin-de-las-delicias-.jpg\n				'),(23294,'http://www.tiendaprado.com/6005-large_default/estuche-de-gafas-el-jardin-de-las-delicias-.jpg\n				'),(23295,'http://www.tiendaprado.com/6950-large_default/bolsa-prado-roja.jpg\n				'),(23296,'http://www.tiendaprado.com/6140-large_default/blue-museo-del-prado-t-shirt-.jpg\n				'),(23297,'http://www.tiendaprado.com/6004-large_default/estuche-de-gafas-el-jardin-de-las-delicias-.jpg\n				'),(23297,'http://www.tiendaprado.com/6005-large_default/estuche-de-gafas-el-jardin-de-las-delicias-.jpg\n				'),(23299,'https://www.britishmuseumshoponline.org/media/catalog/product/cache/926507dc7f93631a094422215b778fe0/t/m/tmpgreek-pots-usb-cmcs53160_productlarge.jpg\n				'),(23299,'https://www.britishmuseumshoponline.org/media/catalog/product/cache/926507dc7f93631a094422215b778fe0/t/m/tmpgreek-pots-usb-cmcs53160_productlargealt1.jpg\n				'),(23300,'https://www.britishmuseumshoponline.org/media/catalog/product/cache/926507dc7f93631a094422215b778fe0/t/m/tmpFuji-Wave-mug-cmcK58030_productlarge.jpg\n				'),(23303,'https://www.hotsox.com/data/default/images/catalog/900/Turnkey/1/xHC000076-BRTPK.png.pagespeed.ic.rKI-hbMHMQ.jpg\n				'),(23305,'https://tienda.guggenheim-bilbao.eus/src/uploads/2017/02/02033448-031.Objetos-tienda_Febrero-2017-cuaderno-guggenheim-puppy-700x700.jpg\n				'),(23305,'https://tienda.guggenheim-bilbao.eus/src/uploads/2017/02/02033447-033.Objetos-tienda_Febrero-2017-cuaderno-guggenheim-puppy-700x700.jpg\n				'),(23305,'https://tienda.guggenheim-bilbao.eus/src/uploads/2017/02/02033449-029.Objetos-tienda_Febrero-2017-cuaderno-guggenheim-puppy-700x700.jpg\n				'),(23306,'https://tienda.guggenheim-bilbao.eus/src/uploads/2014/03/01009151-01-camiseta-puppy-minimil-guggenheim.jpg\n				'),(23306,'https://tienda.guggenheim-bilbao.eus/src/uploads/2014/03/01009151-02-camiseta-puppy-minimil-guggenheim.jpg\n				'),(23308,'https://figurasdeseries.es/1156-large_default/muneco-funko-pop-ahs-hotel-the-countess.jpg\n				'),(23308,'https://images-na.ssl-images-amazon.com/images/I/419G99uAILL.jpg\n				');
/*!40000 ALTER TABLE `product_pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `containsTaboo` bit(1) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `museum_id` int(11) NOT NULL,
  `visitor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_effef8l1gye5hl4fwnqd0lhcy` (`museum_id`),
  KEY `FK_qev81awblncgi904knsb39gbl` (`visitor_id`),
  CONSTRAINT `FK_qev81awblncgi904knsb39gbl` FOREIGN KEY (`visitor_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_effef8l1gye5hl4fwnqd0lhcy` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (23255,0,'Amazing facilities. I would visit it again without a doubt','\0','2018-05-13 23:57:00',5,23206,23243),(23256,0,'One of the best experiences of my life! Sasha Velour\'s performance was like porn for the soul','\0','2018-02-13 23:03:00',5,23206,23244),(23257,0,'The customer service could be improved a little bit more, but I have no complains about the rest of the service.','\0','2018-02-11 19:42:00',4,23206,23248),(23258,0,'This time was not as good as the last one. I\'m a little bit disappointed with the cleaning service.','\0','2018-02-14 20:20:00',3,23206,23243),(23259,0,'I had the worst moments of my life in this place. I wouldn\'t recommend it.','\0','2018-02-14 20:12:00',1,23206,23246),(23260,0,'Problems at sex? Try our brand new viagra. Buy it here: https://www.medexpress.co.uk/clinics/erectile-dysfunction/viagra','','2018-02-13 02:00:00',3,23206,23247),(23261,0,'Watch the best porn videos right here: https://www.youtube.com/watch?v=OUKj92gY-Bs','','2018-02-01 14:27:00',3,23213,23247),(23262,0,'BORING is all I can say about this.','\0','2018-03-02 14:32:00',1,23213,23243),(23263,0,'I fell asleep during a exhibition. I wish I had my money back','\0','2018-03-03 20:24:00',1,23213,23244),(23264,0,'I would prefer watching porn that watching this again','','2018-04-12 09:11:00',2,23213,23248),(23265,0,'It was cute. Nothing else.','\0','2018-01-05 17:44:00',3,23218,23243),(23266,0,'Meh.','\0','2017-12-30 20:48:00',2,23218,23248),(23267,0,'A little bit repetitive to be honest, but it was fun','\0','2018-04-14 23:01:00',4,23220,23244),(23268,0,'FREE SEX HERE Now that I\'ve got yout attention please stream \'Froot\' by Marina and The Diamonds right here: https://open.spotify.com/album/1u2ACTYzVNK3vSLG0Ah4H3?si=HxztptI7TuSsyI8kb8Cz4g','','2017-12-30 20:30:00',3,23220,23247),(23269,0,'Does anyone know where I can buy cialis? Anyways the exhibition was cute','','2018-03-04 11:23:00',3,23230,23246),(23270,0,'Free Sex for those who buy \'Melodrama\' by Lorde on iTunes https://itunes.apple.com/es/album/melodrama/1210856718','','2018-02-10 10:59:00',3,23230,23247),(23271,0,'Cool and scary at the same time! I\'ve never had so much fun!','\0','2018-03-25 15:48:00',5,23239,23243),(23272,0,'I think my girlfriend enjoyed it. Not my case cialis','','2018-03-25 15:54:00',2,23239,23244);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `area` double DEFAULT NULL,
  `inRepair` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `museum_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_py85odw0bsu8uys2kt6m7i4kv` (`museum_id`),
  CONSTRAINT `FK_py85odw0bsu8uys2kt6m7i4kv` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (23207,0,58.9,'\0','A1.10',23206),(23208,0,88.3,'\0','A1.20',23206),(23209,0,30.5,'\0','A1.30',23206),(23210,0,135.78,'\0','SP01',23206),(23211,0,156.78,'','SP02',23206),(23212,0,45.5,'\0','A1.40',23206),(23214,0,30.6,'\0','B2.70',23213),(23215,0,54.12,'','B2.80',23213),(23216,0,124.3,'','SC04',23213),(23219,0,545.11,'','V10.1',23218),(23221,0,45.6,'\0','L4.40',23220),(23222,0,35.88,'\0','L4.50',23220),(23223,0,100.13,'\0','L4.60',23220),(23225,0,450.2,'\0','ST0.0',23224),(23226,0,140.88,'','ST0.1',23224),(23227,0,90.11,'\0','ST0.2',23224),(23229,0,34.5,'\0','G0.12',23228),(23231,0,80.56,'\0','MT0.1-Pop Culture',23230),(23232,0,10.1,'','MT0.2-Sports',23230),(23233,0,30.4,'\0','MT0.3-Politicias',23230),(23234,0,60.68,'\0','MT0.4-Hollywood',23230),(23236,0,15.6,'\0','NG1.0',23235),(23237,0,30.11,'','NG2.0',23235),(23240,0,340.89,'\0','Common',23239),(23241,0,45.65,'\0','SP10',23239);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sponsor`
--

DROP TABLE IF EXISTS `sponsor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsor` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_okfx8h0cn4eidh8ng563vowjc` (`userAccount_id`),
  CONSTRAINT `FK_okfx8h0cn4eidh8ng563vowjc` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sponsor`
--

LOCK TABLES `sponsor` WRITE;
/*!40000 ALTER TABLE `sponsor` DISABLE KEYS */;
INSERT INTO `sponsor` VALUES (23279,0,'2275 Robinson Lane','bombo@gmail.com','MALE','Bombo','+34314202953','Lucas',23133),(23280,0,'2937 Horseshoe Lane','lupe@gmail.com','FEMALE','Lupe','+34216355401','Derr',23134),(23281,0,'1718 Mill Street','parches@gmail.com','MALE','Parches','+34832382160','Cohen',23135),(23282,0,NULL,'orestes@gmail.com','MALE','Orestes','+34314202953','Ross',23136),(23283,0,NULL,'cervasio@gmail.com','MALE','Cervasio','+34667496100','D Long',23155),(23284,0,NULL,'croco@gmail.com','MALE','Croco','+34776112980','Brewer',23156),(23285,0,'3363 Saints Alley','meloni@gmail.com','FEMALE','Méloni','+34665100897','Escamilla',23163),(23286,0,'1508 Davis Place','hamilton@gmail.com','FEMALE','Eloísa','+34655112980','Hamilton',23164),(23287,0,NULL,'avamet@gmail.com','FEMALE','Ava','+34611922780','Metzger',23165),(23288,0,'4193 Jarvis Street','olivia@gmail.com','FEMALE','Olivia','+34651334942','Radcliff',23166),(23289,0,NULL,'arsenio@gmail.com','MALE','Arsenio','+34655222198','Dunn',23167),(23290,0,NULL,'olaf@gmail.com','MALE','Olaf','+34887123500','Sommer',23168);
/*!40000 ALTER TABLE `sponsor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sponsorship`
--

DROP TABLE IF EXISTS `sponsorship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsorship` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `CVV` int(11) DEFAULT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `endingDate` datetime DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `startingDate` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `exhibition_id` int(11) NOT NULL,
  `sponsor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7url5id2hc7muicjogootxy6p` (`exhibition_id`),
  KEY `FK_e3idyfyffpufog3sjl3c2ulun` (`sponsor_id`),
  CONSTRAINT `FK_e3idyfyffpufog3sjl3c2ulun` FOREIGN KEY (`sponsor_id`) REFERENCES `sponsor` (`id`),
  CONSTRAINT `FK_7url5id2hc7muicjogootxy6p` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sponsorship`
--

LOCK TABLES `sponsorship` WRITE;
/*!40000 ALTER TABLE `sponsorship` DISABLE KEYS */;
INSERT INTO `sponsorship` VALUES (23421,0,'https://static1.squarespace.com/static/58b1faf01b631bccf54e27dc/t/59394dfc20099e3818eda73c/1496927754513/Lorde_Website_TourBanner_Main.jpg',555,'Master Card','Bombo',12,'5304731338548557',2021,'2018-05-20 09:00:00','https://lorde.co.nz/tour/','2018-05-14 09:00:00','ACCEPTED',23334,23279),(23422,0,'http://www.tinleyparkamphitheater.com/wp-content/uploads/2015/05/lana-del-rey-banner.png',555,'Master Card','Bombo',12,'5304731338548557',2021,'2018-06-10 20:00:00','http://www.tinleyparkamphitheater.com/events/lana-del-rey/#.WvlgRvmF6t8','2018-05-22 20:00:00','ACCEPTED',23334,23279),(23423,0,'https://pbs.twimg.com/media/DIDLwnKUwAEAps7.jpg',555,'Master Card','Bombo',12,'5304731338548557',2021,'2018-06-30 00:00:00','https://taylorswift.com/','2018-06-15 00:00:00','ACCEPTED',23334,23279),(23424,0,'http://www.generacionpixel.com/wp-content/uploads/2016/05/overwathc.jpg',555,'Master Card','Bombo',12,'5304731338548557',2021,'2018-07-20 00:00:00','http://www.generacionpixel.com/2016/06/05/analisis-overwatch/','2018-07-15 00:00:00','ACCEPTED',23354,23279),(23425,0,'http://queermeup.com/wp-content/uploads/2014/09/Charli-XCX-banner.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'http://queermeup.com/music/charli-xcxs-sophomore-album-sucker-arriving-oct-21-via-neon-goldatlantic/',NULL,'PENDING',23354,23279),(23426,0,'https://esccronicas.files.wordpress.com/2017/07/esc-2018-portugal.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2018-08-27 00:00:00','https://esccronicas.wordpress.com/2017/07/26/eurovision-vuela-de-kiev-a-lisboa-para-2018-en-los-dias-810-y-12-de-mayo/','2018-08-21 00:00:00','TIME_NEGOTIATION',23352,23279),(23427,0,'http://bornonthisway.files.wordpress.com/2011/05/banner.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'https://www.youtube.com/watch?v=wagn8Wrmzuc',NULL,'REJECTED',23352,23279),(23428,0,'https://nancyest.files.wordpress.com/2015/06/cropped-taylor-swift-1989-world-tour-vip-tickets-banner-e1416754637309.jpg',555,'VISA','Lupe',10,'5704414075416561',2025,'2018-07-11 00:00:00','https://www.youtube.com/watch?v=e-ORhEE9VVg','2018-07-01 00:00:00','ACCEPTED',23334,23280),(23429,0,'https://vignette.wikia.nocookie.net/lanadelrey/images/6/6a/Love_Banner.jpg',555,'VISA','Lupe',10,'5704414075416561',2025,'2018-07-20 00:00:00','https://www.youtube.com/watch?v=3-NTv0CdFCk','2018-07-10 00:00:00','ACCEPTED',23356,23280),(23430,0,'https://vignette.wikia.nocookie.net/lanadelrey/images/6/6a/Love_Banner.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2018-01-01 00:00:00','https://www.youtube.com/watch?v=3-NTv0CdFCk','2017-11-01 00:00:00','REJECTED',23357,23282),(23431,0,'http://www.apmusicales.com/wp-content/uploads/Taylor-Swift-1-750x400.jpg',555,'Master Card','Orestes',12,'5156244418841354',2021,'2018-07-01 00:00:00','https://www.youtube.com/watch?v=tCXGJQYZ9JA','2018-05-01 00:00:00','ACCEPTED',23365,23282);
/*!40000 ALTER TABLE `sponsorship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (23291,0,'tiendaprado@gmail.com','http://www.tiendaprado.com/themes/UniqueShop/imgmuseo/logo_tiendaprado_pq.png','Tienda Prado','+34657112980'),(23298,0,'britishshop@gmail.com','https://www.britishmuseumshoponline.org/media/logo/stores/1/logo.png','The Britsh Museum Shop','+34776199100'),(23301,0,'metshop@gmail.com','https://store.metmuseum.org/static/version1526014148/frontend/Corra/metmuseum/en_US/images/logo.svg','The MET Shop','+346651119000'),(23302,0,'louvreshop@gmail.com','https://mir-s3-cdn-cf.behance.net/project_modules/disp/b928d144690415.5607819169ff8.png','The Louvre\'s Shop','+34699887445'),(23304,0,'guggenheimshop@gmail.com','http://payload274.cargocollective.com/1/14/454254/7788719/logo_guggen_900.png','Guggenheim Shop','+34998100456'),(23307,0,'tussaudsshop@gmail.com','http://www.logo-designer.co/wp-content/uploads/2017/05/2017-someone-logo-design-madame-tussauds.png','Madame Tussauds\' Shop','+34887911900'),(23309,0,'egyptianshop@gmail.com','http://uploads.webflow.com/5733a2bde86245b622d7e13f/5745129e9e3261e515a7ec01_Identities-TEMGIP.jpg','The Egyptian Shop','+34667989200');
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `systemconfiguration`
--

DROP TABLE IF EXISTS `systemconfiguration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `systemconfiguration` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `VAT` double DEFAULT NULL,
  `tabooWords` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `systemconfiguration`
--

LOCK TABLES `systemconfiguration` WRITE;
/*!40000 ALTER TABLE `systemconfiguration` DISABLE KEYS */;
INSERT INTO `systemconfiguration` VALUES (23242,0,0.21,'sex|sexo|viagra|cialis|porn');
/*!40000 ALTER TABLE `systemconfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `isLocked` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_csivo9yqa08nrbkog71ycilh5` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount`
--

LOCK TABLES `useraccount` WRITE;
/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` VALUES (23122,0,'\0','21232f297a57a5a743894a0e4a801fc3','admin'),(23123,0,'\0','6b6bd8b6963e4a254bf6d4760390fddc','visitor1'),(23124,0,'\0','dc0aae5ad49b535328e8ff6586ce4cb9','visitor2'),(23125,0,'\0','3c7e8d3dc82fb340495186f57b6f251f','visitor3'),(23126,0,'\0','ecad069456249536c8fb9d96855d59c6','visitor4'),(23127,0,'\0','5157dcb4702627348c8c0539b69a80cf','visitor5'),(23128,0,'\0','56c136b80b32c6bba419115415094b26','visitor6'),(23129,0,'\0','165d185050bab438668aef6faeee3be5','critic1'),(23130,0,'\0','88fc227c1f12e1f11b8b1584991c88fe','critic2'),(23131,0,'\0','8a253092f1d177ba06d104b69a159d93','critic3'),(23132,0,'\0','7ca0505ccfc49a367e7e0e9340d6656d','critic4'),(23133,0,'\0','42c63ad66d4dc07ed17753772bef96d6','sponsor1'),(23134,0,'\0','3dc67f80a03324e01b1640f45d107485','sponsor2'),(23135,0,'\0','857a54956061fdc1b88d7722cafe6519','sponsor3'),(23136,0,'\0','a8be034fe44a453e912feadc414dc803','sponsor4'),(23137,0,'\0','21a70a20a394dc2a038843a51a64322b','director1'),(23138,0,'\0','c9c25d0280cad37aa512f2394f5baa34','director2'),(23139,0,'\0','e3b2adc6c01ef0cdc3520d2b3716dfa3','director3'),(23140,0,'\0','193b0e473341160fead2c20f4120de9a','director4'),(23141,0,'\0','7d2fd72afbe450563de3aed8f7a16c4c','director5'),(23142,0,'\0','287d2662f214126093fdbea4b327825d','director6'),(23143,0,'\0','f510e23d073d307697b845e84f4398f1','guide1'),(23144,0,'\0','f6ea0da319e7c2192998a8b48edc5024','guide2'),(23145,0,'\0','1b60cec032194468b46bd87bfe1af456','guide3'),(23146,0,'\0','50af8909a65fbf6790845e275adecb46','guide4'),(23147,0,'\0','936ccfabcfe7c0c0aa3b2ad77210febd','guide5'),(23148,0,'\0','5ca67f92df774624bdaefc52568b7aa2','guide6'),(23149,0,'\0','792483d3ebafe0965438e3227b93de55','guide7'),(23150,0,'\0','b1df69b415394916d416ffb29d051b52','guide8'),(23151,0,'\0','846827b6b969105dd154b2bbbe4e66ab','guide9'),(23152,0,'\0','944c1a270edbef10d9da4f8aac8221e6','guide10'),(23153,0,'\0','18c7b56054e6b8da311caf45bf3efd79','critic5'),(23154,0,'\0','e5c68eddb605466c498f1391c20ab175','critic6'),(23155,0,'\0','d6d7f85c15bbb451ef50b704b3693033','sponsor5'),(23156,0,'\0','214a700672abaf88da097fbf5f246252','sponsor6'),(23157,0,'','3693575994a2a47ddfd67bd19f37381c','visitor7'),(23158,0,'','237295a49f2b11e7b9415d1a2cd753d6','visitor8'),(23159,0,'','5be811d2f99e53b886b2fcbc95dacec0','visitor9'),(23160,0,'','14afa2b6ad807eed0ce6b4d9e9ab49d6','visitor10'),(23161,0,'','af25ad4009f90b0afa854443f0206eb7','visitor11'),(23162,0,'','785495f4278380480c3f2a63fe06e3d9','visitor12'),(23163,0,'','9798bbc9417e0ad8183332d408703757','sponsor7'),(23164,0,'','8ef13e3e3c71f78fdee0a575c59bbdf5','sponsor8'),(23165,0,'','504c1ba7b26260e8df9e9cd0c36a5fc3','sponsor9'),(23166,0,'','a667d56c80461ae8bd64029e955c60ce','sponsor10'),(23167,0,'','49ea8ae74b49364f5b4740edbe21b99c','sponsor11'),(23168,0,'','bb18323431ed82889eb5d53a0ac376ba','sponsor12');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount_authorities`
--

DROP TABLE IF EXISTS `useraccount_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount_authorities` (
  `UserAccount_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_b63ua47r0u1m7ccc9lte2ui4r` (`UserAccount_id`),
  CONSTRAINT `FK_b63ua47r0u1m7ccc9lte2ui4r` FOREIGN KEY (`UserAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount_authorities`
--

LOCK TABLES `useraccount_authorities` WRITE;
/*!40000 ALTER TABLE `useraccount_authorities` DISABLE KEYS */;
INSERT INTO `useraccount_authorities` VALUES (23122,'ADMINISTRATOR'),(23123,'VISITOR'),(23124,'VISITOR'),(23125,'VISITOR'),(23126,'VISITOR'),(23127,'VISITOR'),(23128,'VISITOR'),(23129,'CRITIC'),(23130,'CRITIC'),(23131,'CRITIC'),(23132,'CRITIC'),(23133,'SPONSOR'),(23134,'SPONSOR'),(23135,'SPONSOR'),(23136,'SPONSOR'),(23137,'DIRECTOR'),(23138,'DIRECTOR'),(23139,'DIRECTOR'),(23140,'DIRECTOR'),(23141,'DIRECTOR'),(23142,'DIRECTOR'),(23143,'GUIDE'),(23144,'GUIDE'),(23145,'GUIDE'),(23146,'GUIDE'),(23147,'GUIDE'),(23148,'GUIDE'),(23149,'GUIDE'),(23150,'GUIDE'),(23151,'GUIDE'),(23152,'GUIDE'),(23153,'CRITIC'),(23154,'CRITIC'),(23155,'SPONSOR'),(23156,'SPONSOR'),(23157,'VISITOR'),(23158,'VISITOR'),(23159,'VISITOR'),(23160,'VISITOR'),(23161,'VISITOR'),(23162,'VISITOR'),(23163,'SPONSOR'),(23164,'SPONSOR'),(23165,'SPONSOR'),(23166,'SPONSOR'),(23167,'SPONSOR'),(23168,'SPONSOR');
/*!40000 ALTER TABLE `useraccount_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitor`
--

DROP TABLE IF EXISTS `visitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitor` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2y6pheswvood223whbrhn006d` (`userAccount_id`),
  CONSTRAINT `FK_2y6pheswvood223whbrhn006d` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor`
--

LOCK TABLES `visitor` WRITE;
/*!40000 ALTER TABLE `visitor` DISABLE KEYS */;
INSERT INTO `visitor` VALUES (23243,0,'1590 Black Stallion Road','tricia@gmail.com','FEMALE','Tricia','+34633017787','Sundberg',23123),(23244,0,'4841 Bagwell Avenue','surfin@gmail.com','MALE','Surfín','+34651980500','Brown',23124),(23245,0,'3826 Leroy Lane','azabache@gmail.com','FEMALE','Azabache','+34677145098','Ford',23125),(23246,0,'1211 Blue Spruce Lane','martita@gmail.com','FEMALE','Martita','+34667208805','C Lee',23126),(23247,0,NULL,'arandano@gmail.com','MALE','Arandano','+34641841970','Posey',23127),(23248,0,NULL,'bruno@gmail.com','MALE','Bruno','+34998841948','Harding',23128),(23249,0,NULL,'beelen@gmail.com','FEMALE','Beelén','+34657198120','Lucky',23157),(23250,0,'4446 Sycamore Fork Road','jota@gmail.com','MALE','Jota','+34652998500','Morrell',23158),(23251,0,'3345 Clearview Drive','flopi@gmail.com','FEMALE','Flopi','+34776900300','McElroy',23159),(23252,0,'2589 Boggess Street','patidifu@gmail.com','FEMALE','Patidifú','+34665111789','Barker',23160),(23253,0,'1435 Radford Street','eto@gmail.com','MALE','Eto','+34661911600','Brown',23161),(23254,0,NULL,'paulino@gmail.com','MALE','Paulino','+34655123780','Sims',23162);
/*!40000 ALTER TABLE `visitor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitor_aggroup`
--

DROP TABLE IF EXISTS `visitor_aggroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitor_aggroup` (
  `participants_id` int(11) NOT NULL,
  `joinedGroups_id` int(11) NOT NULL,
  KEY `FK_sl1nryxkgt6nfi8oxhx0pwdxs` (`joinedGroups_id`),
  KEY `FK_9gdm0pi72wnmooabx800up9p7` (`participants_id`),
  CONSTRAINT `FK_9gdm0pi72wnmooabx800up9p7` FOREIGN KEY (`participants_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_sl1nryxkgt6nfi8oxhx0pwdxs` FOREIGN KEY (`joinedGroups_id`) REFERENCES `aggroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor_aggroup`
--

LOCK TABLES `visitor_aggroup` WRITE;
/*!40000 ALTER TABLE `visitor_aggroup` DISABLE KEYS */;
INSERT INTO `visitor_aggroup` VALUES (23243,23372),(23243,23374),(23243,23375),(23243,23376),(23243,23377),(23243,23382),(23243,23373),(23244,23372),(23244,23373),(23244,23378),(23244,23374),(23244,23377),(23245,23372),(23245,23373),(23246,23379),(23246,23381),(23246,23373),(23246,23375),(23246,23377),(23246,23378),(23246,23383),(23246,23385),(23247,23376),(23247,23373),(23247,23375),(23247,23383),(23247,23384),(23247,23385),(23248,23373),(23248,23376),(23248,23378),(23248,23380);
/*!40000 ALTER TABLE `visitor_aggroup` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-04 12:30:25
