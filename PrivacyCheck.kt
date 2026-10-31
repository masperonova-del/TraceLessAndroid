package com.traceless.privacy.domain

data class PrivacyCheck(
    val id: String,
    val title: String,
    val explanation: String,
    val action: String,
    val category: CheckCategory
)

enum class CheckCategory { PRIVACY, SECURITY, TRACKING }

object CheckCatalog {
    val items = listOf(
        PrivacyCheck(
            "mac_random", "Adresse MAC aléatoire",
            "Sur les réseaux Wi-Fi compatibles, Android peut utiliser une adresse MAC aléatoire. Vérifie ce réglage dans les détails du réseau.",
            "Ouvrir les paramètres Wi-Fi", CheckCategory.PRIVACY
        ),
        PrivacyCheck(
            "private_dns", "DNS privé",
            "Le DNS privé compatible chiffre les requêtes DNS (DNS-over-TLS). Il ne chiffre pas automatiquement tout le trafic Internet.",
            "Ouvrir les paramètres DNS", CheckCategory.PRIVACY
        ),
        PrivacyCheck(
            "browser_history", "Données du navigateur",
            "Vérifie les historiques, cookies et données de site dans le navigateur. Cela ne supprime pas les journaux conservés par un réseau ou un fournisseur.",
            "Consulter le navigateur", CheckCategory.PRIVACY
        ),
        PrivacyCheck(
            "screen_lock", "Verrouillage de l'écran",
            "Utilise un code robuste ou une méthode de verrouillage fiable et configure le verrouillage automatique.",
            "Ouvrir les paramètres de sécurité", CheckCategory.SECURITY
        ),
        PrivacyCheck(
            "app_permissions", "Autorisations des applications",
            "Examine les autorisations sensibles accordées aux applications et retire celles qui ne sont pas nécessaires.",
            "Gérer les autorisations", CheckCategory.SECURITY
        ),
        PrivacyCheck(
            "updates", "Mises à jour Android",
            "Vérifie régulièrement les mises à jour du système et les correctifs de sécurité proposés par le fabricant.",
            "Ouvrir les paramètres système", CheckCategory.SECURITY
        ),
        PrivacyCheck(
            "tracker_awareness", "Suivi par des domaines tiers",
            "Cette version explique le suivi en ligne mais ne surveille pas encore les connexions et ne bloque aucun domaine.",
            "Lire les limites", CheckCategory.TRACKING
        )
    )
}
