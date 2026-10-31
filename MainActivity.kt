package com.traceless.privacy

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF0B1220)
private val Blue = Color(0xFF2563EB)
private val Muted = Color(0xFF64748B)
private val Panel = Color(0xFFF1F5F9)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Blue,
                    background = Color(0xFFF8FAFC),
                    surface = Color.White
                )
            ) {
                TraceLessApp(
                    openPrivateDnsSettings = {
                        try {
                            startActivity(Intent(Settings.ACTION_PRIVATE_DNS_SETTINGS))
                        } catch (_: Exception) {
                            startActivity(Intent(Settings.ACTION_SETTINGS))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TraceLessApp(openPrivateDnsSettings: () -> Unit) {
    var macChecked by remember { mutableStateOf(false) }
    var dnsChecked by remember { mutableStateOf(false) }
    var browserChecked by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(-1) }

    val completed = listOf(macChecked, dnsChecked, browserChecked).count { it }
    val score = completed * 100 / 3

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            Surface(color = Ink, tonalElevation = 0.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("TraceLess", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Assistant de confidentialité Wi-Fi", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(shape = MaterialTheme.shapes.extraLarge, color = Ink) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF93C5FD), modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Bilan de confidentialité", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Text("Configuration guidée · aucune collecte", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                        }
                    }
                    Text("$score %", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(
                        progress = { score / 100f },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Color(0xFF60A5FA),
                        trackColor = Color(0xFF334155)
                    )
                    Text(
                        "Progression du guide uniquement. Ce score ne mesure pas ton invisibilité sur le réseau.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp
                    )
                }
            }

            Text("Vérifications à effectuer", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text("Coche chaque élément après l'avoir vérifié dans les paramètres de ton téléphone.", color = Muted, fontSize = 14.sp)

            ChecklistCard(
                title = "Adresse MAC aléatoire",
                subtitle = "Réduit le suivi fondé sur l'identifiant matériel entre réseaux.",
                checked = macChecked,
                onChecked = { macChecked = it },
                expanded = expanded == 0,
                onExpand = { expanded = if (expanded == 0) -1 else 0 },
                details = "Dans les détails du réseau Wi-Fi enregistré, cherche Confidentialité ou Type d'adresse MAC. Choisis l'adresse MAC aléatoire si Android propose cette option. Les menus varient selon le fabricant. Cela ne rend pas l'appareil invisible au routeur auquel il est connecté."
            )

            ChecklistCard(
                title = "DNS privé",
                subtitle = "Peut chiffrer les requêtes DNS compatibles.",
                checked = dnsChecked,
                onChecked = { dnsChecked = it },
                expanded = expanded == 1,
                onExpand = { expanded = if (expanded == 1) -1 else 1 },
                details = "Le DNS privé Android utilise DNS-over-TLS avec un fournisseur compatible. Il peut réduire la visibilité des requêtes DNS pour certains observateurs, mais ne masque pas l'existence de la connexion ni toutes les métadonnées. Le fournisseur DNS peut traiter les requêtes."
            )
            Button(
                onClick = openPrivateDnsSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Ouvrir les paramètres DNS privé")
            }

            ChecklistCard(
                title = "Historique local du navigateur",
                subtitle = "Les données locales sont distinctes des journaux du routeur.",
                checked = browserChecked,
                onChecked = { browserChecked = it },
                expanded = expanded == 2,
                onExpand = { expanded = if (expanded == 2) -1 else 2 },
                details = "Tu peux gérer l'historique et les cookies depuis le navigateur. Cela n'efface pas les journaux déjà conservés par un routeur, un fournisseur d'accès ou un site Web."
            )

            Surface(shape = MaterialTheme.shapes.large, color = Panel) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Wifi, contentDescription = null, tint = Blue)
                    Spacer(Modifier.width(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("Ce que le Wi-Fi peut encore révéler", fontWeight = FontWeight.SemiBold, color = Ink)
                        Text(
                            "Le réseau peut généralement voir qu'un appareil est connecté, ainsi que des métadonnées comme les horaires, le volume de données et certaines destinations. HTTPS protège normalement le contenu des pages, mais pas toutes les métadonnées.",
                            fontSize = 13.sp,
                            color = Muted
                        )
                    }
                }
            }

            Surface(shape = MaterialTheme.shapes.large, color = Color(0xFFEFF6FF)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Blue)
                    Spacer(Modifier.width(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("Confidentialité, pas invisibilité", fontWeight = FontWeight.SemiBold, color = Ink)
                        Text(
                            "TraceLess ne se connecte à aucun serveur, ne lit pas les journaux du routeur et ne supprime aucune trace à distance. Il ne demande aucune permission réseau dans cette version.",
                            fontSize = 13.sp,
                            color = Muted
                        )
                    }
                }
            }

            Text("Prototype V0.1 · guide éducatif hors ligne", color = Muted, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ChecklistCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    expanded: Boolean,
    onExpand: () -> Unit,
    details: String
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Checkbox(checked = checked, onCheckedChange = onChecked)
                Spacer(Modifier.width(4.dp))
                Column(Modifier.weight(1f).padding(top = 9.dp)) {
                    Text(title, fontWeight = FontWeight.SemiBold, color = Ink)
                    Spacer(Modifier.height(4.dp))
                    Text(subtitle, fontSize = 13.sp, color = Muted)
                }
                TextButton(onClick = onExpand) { Text(if (expanded) "Masquer" else "Détails") }
            }
            if (expanded) {
                Divider(color = Color(0xFFE2E8F0))
                Spacer(Modifier.height(10.dp))
                Text(details, color = Muted, fontSize = 13.sp)
            }
        }
    }
}
