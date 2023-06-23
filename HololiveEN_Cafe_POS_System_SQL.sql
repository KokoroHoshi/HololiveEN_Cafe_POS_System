-- --------------------------------------------------------
-- 主機:                           127.0.0.1
-- 伺服器版本:                        10.11.3-MariaDB - mariadb.org binary distribution
-- 伺服器作業系統:                      Win64
-- HeidiSQL 版本:                  12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 傾印 hololive_en_cafe 的資料庫結構
CREATE DATABASE IF NOT EXISTS `hololive_en_cafe` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `hololive_en_cafe`;

-- 傾印  資料表 hololive_en_cafe.cache_record 結構
CREATE TABLE IF NOT EXISTS `cache_record` (
  `date` varchar(10) NOT NULL,
  `saleNum` int(10) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在傾印表格  hololive_en_cafe.cache_record 的資料：~1 rows (近似值)
INSERT INTO `cache_record` (`date`, `saleNum`) VALUES
	('20230615', 0);

-- 傾印  資料表 hololive_en_cafe.orders_record 結構
CREATE TABLE IF NOT EXISTS `orders_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `saleId` varchar(20) NOT NULL,
  `productId` varchar(10) NOT NULL,
  `productName` varchar(30) NOT NULL,
  `productPrice` int(10) unsigned NOT NULL,
  `productQuantity` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_orders_record_sales_record` (`saleId`),
  KEY `FK_orders_record_product` (`productId`),
  CONSTRAINT `FK_orders_record_product` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_orders_record_sales_record` FOREIGN KEY (`saleId`) REFERENCES `sales_record` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在傾印表格  hololive_en_cafe.orders_record 的資料：~71 rows (近似值)
INSERT INTO `orders_record` (`id`, `saleId`, `productId`, `productName`, `productPrice`, `productQuantity`) VALUES
	(25, 's-id-20230604-1', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(26, 's-id-20230604-2', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(27, 's-id-20230604-2', 'p-d-005', 'Gura愛喝的泡泡浴', 190, 1),
	(28, 's-id-20230604-3', 'p-f-005', '非急勿食!Bloop與應急糧食們', 230, 1),
	(29, 's-id-20230604-3', 'p-f-002', '有勞贖!混沌的起司饗宴', 260, 1),
	(30, 's-id-20230604-3', 'p-f-003', '森大人的粉紅泡泡', 230, 3),
	(31, 's-id-20230605-1', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(32, 's-id-20230605-1', 'p-m-005', '刺繡老帽', 890, 1),
	(33, 's-id-20230606-1', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(34, 's-id-20230606-1', 'p-d-006', '混血天使IRyS的行動要領', 180, 1),
	(35, 's-id-20230607-1', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(36, 's-id-20230607-1', 'p-d-006', '混血天使IRyS的行動要領', 180, 1),
	(37, 's-id-20230607-2', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(38, 's-id-20230607-2', 'p-d-006', '混血天使IRyS的行動要領', 180, 1),
	(39, 's-id-20230607-3', 'p-m-003', '便利貼', 190, 1),
	(40, 's-id-20230607-3', 'p-m-006', '應援毛巾', 450, 1),
	(41, 's-id-20230607-4', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(42, 's-id-20230612-1', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 9),
	(43, 's-id-20230612-1', 'p-f-006', '莓麥喔!混血惡魔的麥片', 190, 7),
	(44, 's-id-20230612-1', 'p-m-002', '大學T', 990, 1),
	(45, 's-id-20230612-1', 'p-m-004', '壓克力磚', 750, 5),
	(46, 's-id-20230612-1', 'p-m-008', '貼紙包', 150, 1),
	(47, 's-id-20230612-2', 'p-m-008', '貼紙包', 150, 1),
	(48, 's-id-20230612-2', 'p-m-006', '應援毛巾', 450, 1),
	(49, 's-id-20230612-2', 'p-f-004', '自然媽媽的沙拉', 210, 1),
	(50, 's-id-20230612-3', 'p-d-006', '混血天使IRyS的行動要領', 180, 1),
	(51, 's-id-20230613-1', 'p-d-002', 'duzzem!Baelz的混沌蘇打', 180, 1),
	(52, 's-id-20230613-1', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(53, 's-id-20230613-2', 'p-m-011', '隨機壓克力合影框', 350, 8),
	(54, 's-id-20230613-2', 'p-m-010', '隨機壓克力立牌', 350, 7),
	(55, 's-id-20230613-2', 'p-m-008', '貼紙包', 150, 16),
	(56, 's-id-20230613-2', 'p-m-009', '滑鼠墊', 790, 9),
	(57, 's-id-20230613-2', 'p-m-012', '隨機壓克力鑰匙圈', 250, 4),
	(58, 's-id-20230613-2', 'p-m-015', '壓克力合影框套組', 1750, 5),
	(59, 's-id-20230613-2', 'p-m-014', '折疊傘', 790, 2),
	(60, 's-id-20230613-2', 'p-m-013', '隨機圓形徽章', 150, 2),
	(61, 's-id-20230613-2', 'p-m-006', '應援毛巾', 450, 9),
	(62, 's-id-20230613-2', 'p-m-004', '壓克力磚', 750, 1),
	(63, 's-id-20230613-2', 'p-m-007', '掛毯', 690, 1),
	(64, 's-id-20230613-2', 'p-m-005', '刺繡老帽', 890, 28),
	(65, 's-id-20230613-2', 'p-f-001', '小心火燭!偵探的炸廚房炒飯', 270, 1),
	(66, 's-id-20230613-2', 'p-f-002', '有勞贖!混沌的起司饗宴', 260, 1),
	(67, 's-id-20230613-2', 'p-f-003', '森大人的粉紅泡泡', 230, 1),
	(68, 's-id-20230613-2', 'p-f-006', '莓麥喔!混血惡魔的麥片', 190, 1),
	(69, 's-id-20230613-2', 'p-f-005', '非急勿食!Bloop與應急糧食們', 230, 1),
	(70, 's-id-20230613-2', 'p-f-004', '自然媽媽的沙拉', 210, 1),
	(71, 's-id-20230613-2', 'p-f-007', 'Wah!今天不吃餅乾!改吃藍莓蛋糕', 230, 2),
	(72, 's-id-20230613-2', 'p-f-008', '店長最愛!KFP唯一指定炸雞', 270, 1),
	(73, 's-id-20230613-2', 'p-f-009', '果汁機禁止!直接吃的墨魚義大利麵', 290, 1),
	(74, 's-id-20230613-2', 'p-f-010', 'Mumei最愛吃的蛋糕', 230, 1),
	(75, 's-id-20230613-2', 'p-d-001', 'Watson名偵探特調', 170, 1),
	(76, 's-id-20230613-2', 'p-d-002', 'duzzem!Baelz的混沌蘇打', 180, 1),
	(77, 's-id-20230613-2', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(78, 's-id-20230613-2', 'p-d-006', '混血天使IRyS的行動要領', 180, 1),
	(79, 's-id-20230613-2', 'p-d-005', 'Gura愛喝的泡泡浴', 190, 1),
	(80, 's-id-20230613-2', 'p-d-004', '德魯伊的治療藥劑', 180, 1),
	(81, 's-id-20230613-2', 'p-d-007', '古神指定!少女祭司的觸手果昔', 180, 1),
	(82, 's-id-20230613-2', 'p-d-008', '店長的無限復活藥水', 180, 1),
	(83, 's-id-20230613-2', 'p-d-009', '時光典獄長的黑暗回憶', 160, 1),
	(84, 's-id-20230613-2', 'p-d-010', '不眠15小!貓頭鷹的提神特調', 170, 1),
	(85, 's-id-20230613-3', 'p-m-003', '便利貼', 190, 23),
	(86, 's-id-20230613-3', 'p-m-002', '大學T', 990, 15),
	(87, 's-id-20230613-3', 'p-m-001', '帆布袋', 290, 23),
	(88, 's-id-20230613-3', 'p-m-006', '應援毛巾', 450, 8),
	(89, 's-id-20230613-3', 'p-m-005', '刺繡老帽', 890, 9),
	(90, 's-id-20230613-3', 'p-m-004', '壓克力磚', 750, 12),
	(91, 's-id-20230613-3', 'p-m-007', '掛毯', 690, 7),
	(92, 's-id-20230613-3', 'p-m-008', '貼紙包', 150, 12),
	(93, 's-id-20230614-1', 'p-d-003', '千杯不醉死神的紅酒替代品', 160, 1),
	(94, 's-id-20230614-1', 'p-d-009', '時光典獄長的黑暗回憶', 160, 1),
	(95, 's-id-20230614-1', 'p-d-010', '不眠15小!貓頭鷹的提神特調', 170, 1);

-- 傾印  資料表 hololive_en_cafe.product 結構
CREATE TABLE IF NOT EXISTS `product` (
  `id` varchar(10) NOT NULL,
  `category` varchar(10) NOT NULL,
  `name` varchar(30) NOT NULL,
  `price` int(10) unsigned NOT NULL DEFAULT 0,
  `imagePath` varchar(30) NOT NULL,
  `description` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在傾印表格  hololive_en_cafe.product 的資料：~38 rows (近似值)
INSERT INTO `product` (`id`, `category`, `name`, `price`, `imagePath`, `description`) VALUES
	('p-d-001', 'Drink', 'Watson名偵探特調', 170, 'Amelia_Drink.png', '奶蓋紅茶'),
	('p-d-002', 'Drink', 'duzzem!Baelz的混沌蘇打', 180, 'Baelz_Drink.png', '蔓越莓汁蘇打'),
	('p-d-003', 'Drink', '千杯不醉死神的紅酒替代品', 160, 'Calliope_Drink.png', '櫻桃可樂'),
	('p-d-004', 'Drink', '德魯伊的治療藥劑', 180, 'Fauna_Drink.png', '情人果冰沙'),
	('p-d-005', 'Drink', 'Gura愛喝的泡泡浴', 190, 'Gura_Drink.png', '香草冰淇淋、柑橘蘇打'),
	('p-d-006', 'Drink', '混血天使IRyS的行動要領', 180, 'IRyS_Drink.png', '石榴葡萄蘇打'),
	('p-d-007', 'Drink', '古神指定!少女祭司的觸手果昔', 180, 'Ina_Drink.png', '藍莓果昔'),
	('p-d-008', 'Drink', '店長的無限復活藥水', 180, 'Kiara_Drink.png', '鹽漬柳橙蘇打'),
	('p-d-009', 'Drink', '時光典獄長的黑暗回憶', 160, 'Kronii_Drink.png', '仙草風味飲'),
	('p-d-010', 'Drink', '不眠15小!貓頭鷹的提神特調', 170, 'Mumei_Drink.png', '玄米拿鐵(含咖啡)'),
	('p-f-001', 'Food', '小心火燭!偵探的炸廚房炒飯', 270, 'Amelia_Food.png', '德式香腸炒飯'),
	('p-f-002', 'Food', '有勞贖!混沌的起司饗宴', 260, 'Baelz_Food.png', '起司紅醬義大利麵'),
	('p-f-003', 'Food', '森大人的粉紅泡泡', 230, 'Calliope_Food.png', '巧克力馬卡龍'),
	('p-f-004', 'Food', '自然媽媽的沙拉', 210, 'Fauna_Food.png', '蘋果生菜沙拉'),
	('p-f-005', 'Food', '非急勿食!Bloop與應急糧食們', 230, 'Gura_Food.png', '奶酪及柑橘果凍'),
	('p-f-006', 'Food', '莓麥喔!混血惡魔的麥片', 190, 'IRyS_Food.png', '莓果燕麥杯'),
	('p-f-007', 'Food', 'Wah!今天不吃餅乾!改吃藍莓蛋糕', 230, 'Ina_Food.png', '藍莓蛋糕'),
	('p-f-008', 'Food', '店長最愛!KFP唯一指定炸雞', 270, 'Kiara_Food.png', '日式唐揚炸雞佐海苔粉及醬料'),
	('p-f-009', 'Food', '果汁機禁止!直接吃的墨魚義大利麵', 290, 'Kronii_Food.png', '墨魚義大利麵'),
	('p-f-010', 'Food', 'Mumei最愛吃的蛋糕', 230, 'Mumei_Food.png', '培茶蛋糕'),
	('p-m-001', 'Merch', '帆布袋', 290, 'ToteBag.png', '帆布袋'),
	('p-m-002', 'Merch', '大學T', 990, 'Crewneck.png', '大學T'),
	('p-m-003', 'Merch', '便利貼', 190, 'StickyNote.png', '便利貼'),
	('p-m-004', 'Merch', '壓克力磚', 750, 'AcrylicBlock.png', '壓克力磚'),
	('p-m-005', 'Merch', '刺繡老帽', 890, 'VintageCap.png', '刺繡老帽'),
	('p-m-006', 'Merch', '應援毛巾', 450, 'SloganTowel.png', '應援毛巾'),
	('p-m-007', 'Merch', '掛毯', 690, 'Tapestry.png', '掛毯'),
	('p-m-008', 'Merch', '貼紙包', 150, 'StickerPack.png', '貼紙包'),
	('p-m-009', 'Merch', '滑鼠墊', 790, 'MouseMat.png', '滑鼠墊'),
	('p-m-010', 'Merch', '隨機壓克力立牌', 350, 'random_AcrylicStand.png', '隨機壓克力立牌'),
	('p-m-011', 'Merch', '隨機壓克力合影框', 350, 'random_AcrylicFrame.png', '隨機壓克力合影框'),
	('p-m-012', 'Merch', '隨機壓克力鑰匙圈', 250, 'random_AcrylicKeychain.png', '隨機壓克力鑰匙圈'),
	('p-m-013', 'Merch', '隨機圓形徽章', 150, 'random_Badge.png', '隨機圓形徽章'),
	('p-m-014', 'Merch', '折疊傘', 790, 'FoldingUmbrella.png', '折疊傘'),
	('p-m-015', 'Merch', '壓克力合影框套組', 1750, 'set_AcrylicFrame.png', '壓克力合影框套組'),
	('p-m-016', 'Merch', '壓克力鑰匙圈套組', 1250, 'set_AcrylicKeychain.png', '壓克力鑰匙圈套組'),
	('p-m-017', 'Merch', '壓克力立牌套組', 1750, 'set_AcrylicStand.png', '壓克力立牌套組'),
	('p-m-018', 'Merch', '徽章套組', 750, 'set_Badge.png', '徽章套組');

-- 傾印  資料表 hololive_en_cafe.sales_record 結構
CREATE TABLE IF NOT EXISTS `sales_record` (
  `id` varchar(30) NOT NULL,
  `date` datetime NOT NULL DEFAULT current_timestamp(),
  `totalPrice` int(10) unsigned NOT NULL,
  `paymentMethod` varchar(10) DEFAULT NULL,
  `LINE_PayCode` varchar(30) DEFAULT NULL,
  `creditCardCode` varchar(30) DEFAULT NULL,
  `discountCode` varchar(30) DEFAULT NULL,
  `carrierCode` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在傾印表格  hololive_en_cafe.sales_record 的資料：~16 rows (近似值)
INSERT INTO `sales_record` (`id`, `date`, `totalPrice`, `paymentMethod`, `LINE_PayCode`, `creditCardCode`, `discountCode`, `carrierCode`) VALUES
	('s-id-20230604-1', '2023-06-04 16:15:41', 160, 'Cash', '', '', '', ''),
	('s-id-20230604-2', '2023-06-04 16:17:29', 350, 'Cash', '', '', '', ''),
	('s-id-20230604-3', '2023-06-04 16:21:16', 944, 'CreditCard', '', '789', '78', ''),
	('s-id-20230605-1', '2023-06-05 17:48:57', 1050, 'Cash', '', '', '', ''),
	('s-id-20230606-1', '2023-06-06 05:36:44', 340, 'Cash', '', '', '', ''),
	('s-id-20230607-1', '2023-06-07 08:15:07', 340, 'Cash', '', '', '', ''),
	('s-id-20230607-2', '2023-06-07 08:46:33', 340, 'Cash', '', '', '', ''),
	('s-id-20230607-3', '2023-06-07 08:47:26', 640, 'Cash', '', '', '', ''),
	('s-id-20230607-4', '2023-06-07 08:49:21', 160, 'LINE_Pay', '89', '', '', ''),
	('s-id-20230612-1', '2023-06-12 08:38:42', 6128, 'Cash', '', '', '90', ''),
	('s-id-20230612-2', '2023-06-12 15:21:29', 810, 'Cash', '', '', '', ''),
	('s-id-20230612-3', '2023-06-12 15:23:36', 180, 'Cash', '', '', '', ''),
	('s-id-20230613-1', '2023-06-13 21:11:09', 340, 'LINE_Pay', 'gh', '', '', ''),
	('s-id-20230613-2', '2023-06-13 21:14:28', 61190, 'Cash', '', '', '', ''),
	('s-id-20230613-3', '2023-06-13 21:15:48', 53130, 'Cash', '', '', '', ''),
	('s-id-20230614-1', '2023-06-14 15:32:24', 490, 'Cash', '', '', '', '');

-- 傾印  資料表 hololive_en_cafe.staff 結構
CREATE TABLE IF NOT EXISTS `staff` (
  `id` varchar(10) NOT NULL,
  `department` varchar(30) NOT NULL,
  `name` varchar(30) NOT NULL,
  `imagePath` varchar(30) NOT NULL,
  `description` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在傾印表格  hololive_en_cafe.staff 的資料：~12 rows (近似值)
INSERT INTO `staff` (`id`, `department`, `name`, `imagePath`, `description`) VALUES
	('s-C-00', 'Cover', '谷郷元昭', 'Cover/Yagoo.png', 'Motoaki Tanigou (谷郷元昭) (birthday: December 10), also known as YAGOO or "the best girl" in hololive, is the CEO of Cover Corporation, the company behind the VTuber agency hololive production.'),
	('s-c-01', 'Council', 'Ceres Fauna', 'Council/Fauna.png', 'Keeper of "Nature"\r\n\r\nA member of the Council and the Keeper of "Nature," the second concept created by the Gods.\r\nShe has materialized in the mortal realm as a druid in a bid to save nature.\r\nShe has Kirin blood flowing in her veins, and horns that are made out of the branches of a certain tree; they are NOT deer antlers.\r\n\r\n"Nature" refers to all organic matter on the planet except mankind.\r\nIt is long said that her whispers, as an avatar of Mother Nature, have healing properties. Whether or not that is true is something only those who have heard them can say.\r\nWhile she is usually affable, warm, and slightly mischievous, any who anger her will bear the full brunt of Nature\'s fury.'),
	('s-c-02', 'Council', 'Ouro Kronii', 'Council/Kronii.png', 'Warden of "Time"\r\n\r\nA member of the Council and the Warden of "Time," the third concept birthed by the Gods and the one most intrinsically linked with mankind.\r\nNone may escape the sands of time and, indeed, most men do not even wish to be freed from her captivity. She picked up the blades almost as a reflex action of sorts, and seems to be enjoying it. Her quiet, aloof personality has never changed over the ages, but she has developed a little bit of haughtiness and sadistic tendencies along the way...\r\n\r\nOriginally, the concept of time was but a cog in the wheel, one which only functioned in tandem with others.\r\nEventually, humans went on to give time individual meanings such as dawn and dusk, making it universal. The humans became enslaved to Time in return for empowering her. In her present form, she has become a sadistic, prideful warden.'),
	('s-c-03', 'Council', 'Nanashi Mumei', 'Council/Mumei.png', 'Guardian of "Civilization"\r\n\r\nA member of the Council and the Guardian of "Civilization," a concept crafted by mankind.\r\nAs a living embodiment of the sum of mankind\'s efforts—the mark that humans have left on the world—she is far removed from her fellow members, as well as other lifeforms. Due to not being created by the Gods, she was free to choose her own appearance, and decided to make herself owl-like, after the bird that symbolizes wisdom.\r\n\r\nShe is gentle, wise, and an unbelievably hard worker. As a well-traveled vagabond, she is blessed with a wealth of knowledge of the world. She has seen, heard, and experienced so many things that she has forgotten most of them, one of them being her own name.\r\n\r\nFor some reason, she seems to project a rather pitiable aura. Perhaps this is in part thanks to the loneliness she has often felt in her perennial travels. That is what gave her the idea of making her own friend out of a material that was indispensable to the development of human civilization: paper.\r\n\r\n"It may fade and rip, but once a friend, forever a friend."'),
	('s-c-04', 'Council', 'Hakos Baelz', 'Council/Baelz.png', 'Chaos\r\n\r\nA member of the Council and the very concept of Chaos, birthed by the world, itself.\r\nThe Gods appointed her Chairperson of the Council, but she mostly takes a hands-off approach as she has yet to accept the decision, much like her fellow members. That said, all of them have no intention of contending; instead to obey.\r\n\r\nHer mouse-like appearance has deceived many a fool who fails to realize that mice are the harbingers of chaos.\r\nAlso of note is her wild smile, which she often flashes in her frequent romps.\r\n\r\nChaos is confusion and disarray, it is fortune and tragedy, and it is freedom from all the logic and inherent nature of life.\r\nShe believes that rules are not the be-all and end-all, which is why she has come to break them all.\r\nWatching the aftermath is her greatest joy, and so she remains a bystander to the destruction caused by mayhem.'),
	('s-c-05', 'Council', 'Tsukumo Sana', 'Council/Sana.png', 'Speaker of "Space”\r\n\r\nA member of the Council and the Speaker of "Space," the very first concept created by the Gods.\r\nAfter being materialized in the mortal realm, she began researching astrology in earnest for one particular reason. However, she soon stumbled unknowingly upon the Forbidden Truth, and was turned into an apostle for the Ancient Ones.\r\n\r\n"Space" refers not only to this universe we live in, but all dimensions and matter to exist. It is a concept unbridled by definition, and one that continues to grow in scope limitlessly. As it is yet to be fully explored by man, her seemingly strange behavior and speech can be explained away with a simple, "Humanity simply isn\'t yet ready to understand."\r\n\r\nAs a side note, despite how she may look, she is quite chi... young.'),
	('s-m-01', 'Myth', 'Mori Calliope', 'Myth/Calliope.png', 'The Grim Reaper\'s first apprentice. Due to modern medical care causing a decline in the reaping business, Calliope decided to become a VTuber to harvest souls instead. It seems that the ascended souls of the people who are vaporized by the wholesome interactions between VTubers go to her as well.\r\nThat being said, despite the image her hardcore vocals and manner of speech gives off, she\'s actually a gentle-hearted girl who cares greatly for her friends.'),
	('s-m-02', 'Myth', 'Takanashi Kiara', 'Myth/Kiara.png', 'An idol whose dream is to become the owner of a fast food chain. Kiara is a phoenix, not a chicken or turkey. (Very important)\r\nShe burns brightly, working herself to the bone since she\'ll just be reborn from her ashes anyway.'),
	('s-m-03', 'Myth', 'Ninomae Ina\'nis', 'Myth/Ina.png', 'Despite her looks, Ina\'nis is actually a priestess of the Ancient Ones. One day, she picked up a strange book and then started to gain the power of controlling tentacles. To her, tentacles are just a part in her ordinary life; it has never been a big deal for her. However, her girly mind does want to get them dressed up and stay pretty.\r\nAfter gaining power, she started hearing Ancient Whispers and Revelations. Hence, she began her VTuber activities to deliver random sanity checks on humanity, as an ordinary girl.'),
	('s-m-04', 'Myth', 'Gawr Gura', 'Myth/Gura.png', 'A descendant of the Lost City of Atlantis, who swam to Earth while saying, "It\'s so boring down there LOLOLOL!"\r\nGura bought her clothes (and her shark hat) in the human world and she really loves them. In her spare time, she enjoys talking to marine life.'),
	('s-m-05', 'Myth', 'Watson Amelia', 'Myth/Amelia.png', 'Amelia heard strange rumors online surrounding hololive: talking foxes, magical squirrels, superhuman dogs, and more. Soon after beginning her investigation on hololive, and just out of interest, she decided to become an idol herself!\r\nShe loves to pass her time training her reflexes with FPS games, and challenging herself with puzzle games. "It\'s elementary, right?"'),
	('s-p-01', 'Project: HOPE', 'IRyS', 'Project_Hope/IRyS.png', 'Diva of hololive English Project: HOPE IRyS\r\n\r\nIRyS, a half-demon, half-angel also known as a Nephilim, once brought hope to "The Paradise" during the ancient age. Her full name is ███████ Irys. This current era, riddled with despair and desperation, has brought upon her second awakening. She has arrived to deliver hope, with her feelings put into her lyrics and songs, and determination in her voice.\r\nThough she tends to retain her Nephilim form, she is still quite young within her race. With emotion and "other factors," it is possible that her form may lean to either side. When that occurs, she bears a brief change in her appearance.\r\nShe does not speak of the events that preceded her second awakening. She faces ever forward, perhaps because she is Hope Incarnate, though no one knows for sure. Or could it be...\r\n"The future is not what helps people; the future is made with our own hands.\r\nSalvation is hypocrisy. If people know they are to be saved, they simply will not try.\r\nFrom the depths of despair: a glimmer of light, the warmth of recovery.\r\nAs long as we are with hope, we live. We stand tall."');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
