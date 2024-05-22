SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
-- Base de données : `agence`
-- --------------------------------------------------------
-- Structure de la table `agents`
CREATE TABLE `agents` (
  `nom` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Déchargement des données de la table `agents`
INSERT INTO `agents` (`nom`) VALUES
('aa'),
('Agent 1'),
('akrour'),
('aya'),
('iii'),
('OumElKheir'),

-- --------------------------------------------------------
-- Structure de la table `agent_biens`
CREATE TABLE `agent_biens` (
  `id` int(11) NOT NULL,
  `id_bien` int(11) DEFAULT NULL,
  `nom_agent` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Déchargement des données de la table `agent_biens`
INSERT INTO `agent_biens` (`id`, `id_bien`, `nom_agent`) VALUES
(1, 1, 'Agent 1'),
(2, 5, 'Agent 1'),
(3, 6, 'yyyyyy'),
-- --------------------------------------------------------
-- Structure de la table `biens`
CREATE TABLE `biens` (
  `id` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `taille` double DEFAULT NULL,
  `prix` double DEFAULT NULL,
  `localisation` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `agentImmobilier` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Déchargement des données de la table `biens`
INSERT INTO `biens` (`id`, `type`, `taille`, `prix`, `localisation`, `description`, `agentImmobilier`) VALUES
(1, 'VILLA', 100, 200000, 'Paris sain jermain', 'Bel appartement', NULL),
(2, 'APPARTEMENT', 80, 150000, 'rouiba', 'Appartement lumineux en centre-ville', NULL),
(5, 'HANGAR', 1000000, 100000, 'rouiba', 'grand hangar', NULL),
(6, 'VILLA', 6, 6, 'aa', 'aab', NULL),
(7, 'APPARTEMENT', 777, 999, 'aaa', 'ttt', NULL),
(10, 'APPARTEMENT', 1000, 12000, 'alger', 'bel appartement', NULL);
-- --------------------------------------------------------
-- Structure de la table `clients`
CREATE TABLE `clients` (
  `nom` varchar(255) NOT NULL,
  `TypeClient` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Déchargement des données de la table `clients`
INSERT INTO `clients` (`nom`, `TypeClient`) VALUES
('dd', 'VENDEUR'),
('uuu', 'ACHETEUR');
-- --------------------------------------------------------
-- Structure de la table `demandes_client`
CREATE TABLE `demandes_client` (
  `id` int(11) NOT NULL,
  `client` varchar(255) DEFAULT NULL,
  `typeBienRecherche` varchar(255) DEFAULT NULL,
  `budgetMax` double DEFAULT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Déchargement des données de la table `demandes_client`
INSERT INTO `demandes_client` (`id`, `client`, `typeBienRecherche`, `budgetMax`, `description`) VALUES
(2, 'dd', 'VILLA', 55555, 'aaaa'),
(3, 'dd', 'APPARTEMENT', 55555, 'aaaa'),
(4, 'dd', 'VILLA', 66666, 'taattaa'),
(5, 'dd', 'MAISON', 70000, 'BELLE');
-- --------------------------------------------------------
-- Structure de la table `interactions`
CREATE TABLE `interactions` (
  `id` int(11) NOT NULL,
  `date` datetime DEFAULT NULL,
  `idClient` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- --------------------------------------------------------
-- Structure de la table `rendez_vous`
CREATE TABLE `rendez_vous` (
  `id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `heure` varchar(255) DEFAULT NULL,
  `bien` int(11) DEFAULT NULL,
  `client` varchar(255) DEFAULT NULL,
  `agent` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Déchargement des données de la table `rendez_vous`
INSERT INTO `rendez_vous` (`id`, `date`, `heure`, `bien`, `client`, `agent`) VALUES
(1, '2020-10-10', '10:10', 1, 'dd', 'aa'),
(2, '2020-12-23', '13:20', 5, 'dd', 'Agent 4'),
(3, '2020-10-12', '12:08', 5, 'dd', 'Agent 2');
-- --------------------------------------------------------
-- Structure de la table `temp_biens`
CREATE TABLE `temp_biens` (
  `id` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `taille` double DEFAULT NULL,
  `prix` double DEFAULT NULL,
  `localisation` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Déchargement des données de la table `temp_biens`
INSERT INTO `temp_biens` (`id`, `type`, `taille`, `prix`, `localisation`, `description`) VALUES
(2, 'APPARTEMENT', 80, 150000, 'rouiba', 'Appartement lumineux en centre-ville'),
(7, 'APPARTEMENT', 777, 999, 'aaa', 'ttt'),
(10, 'APPARTEMENT', 1000, 12000, 'alger', 'bel appartement');
-- ----------------------------------------------------
-- Structure de la table `transactions`
CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `prix` double DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `bien` int(11) DEFAULT NULL,
  `client` varchar(255) DEFAULT NULL,
  `agent` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Déchargement des données de la table `transactions`
INSERT INTO `transactions` (`id`, `date`, `prix`, `type`, `bien`, `client`, `agent`) VALUES
(1, '2024-05-06', 22222, 'VENTE', 1, 'dd', 'aa'),
(3, '2024-05-06', 7000000, 'VENTE', 1, 'dd', 'aa'),
(5, '2024-05-07', 10000000, 'LOCATION', 2, 'dd', 'Agent 1'),
(6, '2024-05-07', 900000, 'VENTE', 1, 'dd', 'aa'),
(7, '2024-05-07', 40000, 'VENTE', 1, 'dd', 'aa'),
(8, '2024-05-10', 70000, 'LOCATION', 7, 'dd', 'Agent 4');
-- Index pour les tables déchargées
-- Index pour la table `agents`
ALTER TABLE `agents`
  ADD PRIMARY KEY (`nom`);
-- Index pour la table `agent_biens`
ALTER TABLE `agent_biens`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_bien` (`id_bien`),
  ADD KEY `nom_agent` (`nom_agent`);
-- Index pour la table `biens`
ALTER TABLE `biens`
  ADD PRIMARY KEY (`id`),
  ADD KEY `agentImmobilier` (`agentImmobilier`);
-- Index pour la table `clients`
ALTER TABLE `clients`
  ADD PRIMARY KEY (`nom`);
-- Index pour la table `demandes_client`
ALTER TABLE `demandes_client`
  ADD PRIMARY KEY (`id`),
  ADD KEY `client` (`client`);
-- Index pour la table `interactions`
ALTER TABLE `interactions`
  ADD PRIMARY KEY (`id`);
-- Index pour la table `rendez_vous`
ALTER TABLE `rendez_vous`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bien` (`bien`),
  ADD KEY `client` (`client`),
  ADD KEY `agent` (`agent`);
-- Index pour la table `transactions`
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bien` (`bien`),
  ADD KEY `client` (`client`),
  ADD KEY `agent` (`agent`);
-- AUTO_INCREMENT pour les tables déchargées
-- AUTO_INCREMENT pour la table `agent_biens`
ALTER TABLE `agent_biens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
-- AUTO_INCREMENT pour la table `biens`
ALTER TABLE `biens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;
-- AUTO_INCREMENT pour la table `demandes_client`
ALTER TABLE `demandes_client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
-- AUTO_INCREMENT pour la table `interactions`
ALTER TABLE `interactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
-- AUTO_INCREMENT pour la table `rendez_vous`
ALTER TABLE `rendez_vous`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
-- AUTO_INCREMENT pour la table `transactions`
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
-- Contraintes pour les tables déchargées
-- Contraintes pour la table `agent_biens`
ALTER TABLE `agent_biens`
  ADD CONSTRAINT `agent_biens_ibfk_1` FOREIGN KEY (`id_bien`) REFERENCES `biens` (`id`),
  ADD CONSTRAINT `agent_biens_ibfk_2` FOREIGN KEY (`nom_agent`) REFERENCES `agents` (`nom`);
-- Contraintes pour la table `biens`
ALTER TABLE `biens`
  ADD CONSTRAINT `biens_ibfk_1` FOREIGN KEY (`agentImmobilier`) REFERENCES `agents` (`nom`);
-- Contraintes pour la table `demandes_client`
ALTER TABLE `demandes_client`
  ADD CONSTRAINT `demandes_client_ibfk_1` FOREIGN KEY (`client`) REFERENCES `clients` (`nom`);
-- Contraintes pour la table `rendez_vous`
ALTER TABLE `rendez_vous`
  ADD CONSTRAINT `rendez_vous_ibfk_1` FOREIGN KEY (`bien`) REFERENCES `biens` (`id`),
  ADD CONSTRAINT `rendez_vous_ibfk_2` FOREIGN KEY (`client`) REFERENCES `clients` (`nom`),
  ADD CONSTRAINT `rendez_vous_ibfk_3` FOREIGN KEY (`agent`) REFERENCES `agents` (`nom`);
-- Contraintes pour la table `transactions`
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`bien`) REFERENCES `biens` (`id`),
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`client`) REFERENCES `clients` (`nom`),
  ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`agent`) REFERENCES `agents` (`nom`);
COMMIT;