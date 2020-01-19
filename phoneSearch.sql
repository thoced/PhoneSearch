-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le :  Dim 19 jan. 2020 à 10:06
-- Version du serveur :  10.3.11-MariaDB
-- Version de PHP :  5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `phoneSearch`
--

-- --------------------------------------------------------

--
-- Structure de la table `t_base_dossiers`
--

CREATE TABLE `t_base_dossiers` (
  `id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `section` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Déchargement des données de la table `t_base_dossiers`
--

INSERT INTO `t_base_dossiers` (`id`, `name`, `section`) VALUES
(1, 'LI_BARTOLO', 1),
(2, 'LI_WOLVERINE', 4),
(3, 'LI_ROCCO', 3),
(4, 'LI_GITANS', 5),
(5, 'LI_ALEXANDRE_LE_GRAND', 2);

-- --------------------------------------------------------

--
-- Structure de la table `t_base_identities`
--

CREATE TABLE `t_base_identities` (
  `id` int(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `firstName` varchar(100) NOT NULL,
  `dateOfBirth` varchar(10) NOT NULL,
  `bng` varchar(5) NOT NULL,
  `postCode` int(4) NOT NULL,
  `city` varchar(100) NOT NULL,
  `street` varchar(255) NOT NULL,
  `number` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Déchargement des données de la table `t_base_identities`
--

INSERT INTO `t_base_identities` (`id`, `name`, `firstName`, `dateOfBirth`, `bng`, `postCode`, `city`, `street`, `number`) VALUES
(1, 'Vivroux', 'Geoffrey', '30/08/1981', 'CEMSV', 4140, 'Sprimont', 'rue de Louveigné', 64),
(2, 'Thonon', 'Cedric', '01/01/2001', 'C', 0, '', '', 0),
(3, 'Coucke', 'Frederik', '02/02/2002', 'SC', 0, '', '', 0),
(4, 'test', 'jean louis', '30/08/1981', 'V', 0, '', '', 0),
(5, 'lopes gomez', 'paolo', '01/01/2001', 'M', 0, '', '', 0),
(6, 'lopes gomez', 'jean louis', '', 'E', 0, '', '', 0),
(7, 'test', 'jean-louis', '', '', 0, '', '', 0);

-- --------------------------------------------------------

--
-- Structure de la table `t_base_registers`
--

CREATE TABLE `t_base_registers` (
  `id` int(3) NOT NULL,
  `unit` int(4) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `t_base_registers`
--

INSERT INTO `t_base_registers` (`id`, `unit`, `name`) VALUES
(1, 5278, 'ZP Seraing/Neupré'),
(2, 5277, 'ZP Liège');

-- --------------------------------------------------------

--
-- Structure de la table `t_base_sections`
--

CREATE TABLE `t_base_sections` (
  `id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Déchargement des données de la table `t_base_sections`
--

INSERT INTO `t_base_sections` (`id`, `name`) VALUES
(1, 'Stups'),
(2, 'Ecofin'),
(3, 'Moeurs'),
(4, 'Crimes'),
(5, 'Vols');

-- --------------------------------------------------------

--
-- Structure de la table `t_register_5277`
--

CREATE TABLE `t_register_5277` (
  `id` int(11) NOT NULL,
  `identity` int(50) NOT NULL,
  `phoneNumber` varchar(11) NOT NULL,
  `source` varchar(50) NOT NULL,
  `reference` int(6) NOT NULL,
  `year` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Déchargement des données de la table `t_register_5277`
--

INSERT INTO `t_register_5277` (`id`, `identity`, `phoneNumber`, `source`, `reference`, `year`) VALUES
(1, 3, '32987654321', 'FNC enquête', 1234, 2012),
(2, 2, '32496687368', 'bla', 123, 2013),
(3, 3, '32496687368', 'pwet', 23, 2000),
(4, 4, '32123123', 'ZP5278', 123, 2012),
(5, 5, '32789456', 'courrier ', 1234, 2000);

-- --------------------------------------------------------

--
-- Structure de la table `t_register_5278`
--

CREATE TABLE `t_register_5278` (
  `id` int(10) NOT NULL,
  `identity` int(50) NOT NULL,
  `phoneNumber` varchar(11) NOT NULL,
  `source` varchar(50) NOT NULL,
  `reference` int(6) NOT NULL,
  `year` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Déchargement des données de la table `t_register_5278`
--

INSERT INTO `t_register_5278` (`id`, `identity`, `phoneNumber`, `source`, `reference`, `year`) VALUES
(1, 1, '32496687368', 'judiciaire non roulage', 1234, 2016),
(2, 2, '32123456789', 'courrier ', 4321, 2012),
(4, 1, '32496687368', 'Courrier', 1234, 2019),
(5, 1, '32123456789', 'bla', 123, 19),
(6, 6, '32496687368', 'ZP5278', 123, 2012),
(7, 7, '3212345678', 'courrier ', 1234, 2001);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `t_base_dossiers`
--
ALTER TABLE `t_base_dossiers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `contrainte01` (`section`);

--
-- Index pour la table `t_base_identities`
--
ALTER TABLE `t_base_identities`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `t_base_registers`
--
ALTER TABLE `t_base_registers`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `t_base_sections`
--
ALTER TABLE `t_base_sections`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `t_register_5277`
--
ALTER TABLE `t_register_5277`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `t_register_5278`
--
ALTER TABLE `t_register_5278`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `t_base_dossiers`
--
ALTER TABLE `t_base_dossiers`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `t_base_identities`
--
ALTER TABLE `t_base_identities`
  MODIFY `id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `t_base_registers`
--
ALTER TABLE `t_base_registers`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `t_base_sections`
--
ALTER TABLE `t_base_sections`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `t_register_5277`
--
ALTER TABLE `t_register_5277`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `t_register_5278`
--
ALTER TABLE `t_register_5278`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `t_base_dossiers`
--
ALTER TABLE `t_base_dossiers`
  ADD CONSTRAINT `contrainte01` FOREIGN KEY (`section`) REFERENCES `t_base_sections` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
