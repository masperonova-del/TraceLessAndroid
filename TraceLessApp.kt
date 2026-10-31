package com.traceless.privacy.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.traceless.privacy.data.LocalPreferences
import com.traceless.privacy.domain.CheckCatalog
import com.traceless.privacy.domain.CheckCategory
import com.traceless.privacy.domain.PrivacyCheck
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Ink = androidx.compose.ui.graphics.Color(0xFF101827)
private val Blue = androidx.compose.ui.graphics.Color(0xFF2563EB)
private val SurfaceDark = androidx.compose.ui.graphics.Color(0xFF172235)

@Composable
fun TraceLessApp() {
    val context = LocalContext.current
    val store = remember { LocalPreferences(context.applicationContext) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var refresh by remember { mutableIntStateOf(0) }

    val checks = remember(refresh) { CheckCatalog.items }
    val completed = checks.count { store.isChecked(it.id) }
    val progress = if (checks.isEmpty()) 0f else completed.toFloat() / checks.size

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFF7DA7FF),
            secondary = androidx.compose.ui.graphics.Color(0xFF8BD5CA),
            background = Ink,
            surface = SurfaceDark
        )
    ) {
        Scaffold(
            containerColor = Ink,
            bottomBar = {
                NavigationBar(containerColor = SurfaceDark) {
                    val tabs = listOf(
                        "Accueil" to Icons.Default.Dashboard,
                        "Confidentialité" to Icons.Default.Visibility,
                        "Sécurité" to Icons.Default.Shield,
                        "Traçage" to Icons.Default.Radar,
                        "Rapports" to Icons.Default.Assessment
                    )
                    tabs.forEachIndexed { index, pair ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Icon(pair.second, contentDescription = pair.first) },
                            label = { Text(pair.first, maxLines = 1) }
                        )
                    }
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> Dashboard(
                    modifier = Modifier.padding(padding),
                    completed = completed,
                    total = checks.size,
                    progress = progress,
                    onOpenPrivacy = { selectedTab = 1 },
                    onOpenSecurity = { selectedTab = 2 }
                )
                1 -> CheckListScreen(
                    Modifier.padding(padding),
                    "Confidentialité",
                    checks.filter { it.category == CheckCategory.PRIVACY },
                    store, { refresh++ }
                )
                2 -> CheckListScreen(
                    Modifier.padding(padding),
                    "Sécurité",
                    checks.filter { it.category == CheckCategory.SECURITY },
                    store, { refresh++ }
                )
                3 -> TrackingScreen(Modifier.padding(padding))
                else -> ReportsScreen(Modifier.padding(padding), store, completed, checks.size)
            }
        }
    }
}

@Composable
private fun Dashboard(
    modifier: Modifier,
    completed: Int,
    total: Int,
    progress: Float,
    onOpenPrivacy: () -> Unit,
    onOpenSecurity: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("TRACELESS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text("Votre confidentialité,
à votre rythme.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Un assistant local pour comprendre et améliorer vos réglages Android.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = SurfaceDark), shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FactCheck, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Progression de votre guide", fontWeight = FontWeight.SemiBold)
                    }
                    Text("$completed / $total contrôles confirmés", style = MaterialTheme.typography.titleLarge)
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                    Text("Ce chiffre mesure les étapes que vous avez confirmées. Ce n'est pas une note de sécurité ni une preuve d'anonymat.",
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item { SectionButton("Confidentialité", "Wi-Fi, DNS et données locales", Icons.Default.Visibility, onOpenPrivacy) }
        item { SectionButton("Sécurité Android", "Verrouillage, autorisations et mises à jour", Icons.Default.Shield, onOpenSecurity) }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFF332B1B))) {
                Column(Modifier.padding(16.dp)) {
                    Text("À savoir", fontWeight = FontWeight.Bold, color = androidx.compose.ui.graphics.Color(0xFFFFD58A))
                    Text("TraceLess ne rend pas votre téléphone invisible. Les réseaux, sites et applications peuvent encore traiter certaines informations.")
                }
            }
        }
    }
}

@Composable
private fun SectionButton(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(onClick = onClick, colors = CardDefaults.cardColors(containerColor = SurfaceDark), shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
private fun CheckListScreen(
    modifier: Modifier,
    title: String,
    items: List<PrivacyCheck>,
    store: LocalPreferences,
    onChanged: () -> Unit
) {
    val context = LocalContext.current
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Lisez chaque explication, vérifiez le réglage sur votre appareil, puis cochez uniquement si vous l'avez réellement contrôlé.",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(items, key = { it.id }) { item ->
            var checked by remember(item.id) { mutableStateOf(store.isChecked(item.id)) }
            Card(colors = CardDefaults.cardColors(containerColor = SurfaceDark), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Checkbox(checked = checked, onCheckedChange = {
                            checked = it
                            store.setChecked(item.id, it)
                            onChanged()
                        })
                    }
                    Text(item.explanation, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                    OutlinedButton(onClick = { openRelevantSettings(context, item.id) }) {
                        Text(item.action)
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Default.OpenInNew, contentDescription = null)
                    }
                    Text(if (checked) "Contrôle confirmé par vous" else "Pas encore confirmé",
                        color = if (checked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

private fun openRelevantSettings(context: android.content.Context, id: String) {
    val intent = when (id) {
        "mac_random" -> Intent(Settings.ACTION_WIFI_SETTINGS)
        "private_dns" -> Intent("android.settings.PRIVATE_DNS_SETTINGS").let {
            if (it.resolveActivity(context.packageManager) != null) it else Intent(Settings.ACTION_SETTINGS)
        }
        "screen_lock", "updates" -> Intent(Settings.ACTION_SECURITY_SETTINGS).let {
            if (it.resolveActivity(context.packageManager) != null) it else Intent(Settings.ACTION_SETTINGS)
        }
        "app_permissions" -> Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.parse("package:" + context.packageName)
        }
        else -> Intent(Settings.ACTION_SETTINGS)
    }
    runCatching { context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
}

@Composable
private fun TrackingScreen(modifier: Modifier) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Anti-traçage", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Radar, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    Text("Module en préparation", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Cette version ne capture pas les connexions, ne détecte pas automatiquement les trackers et ne bloque aucun domaine.")
                    Text("La prochaine étape sera un moteur local facultatif basé sur Android VpnService, avec consentement explicite et sans serveur VPN distant.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            Text("Pourquoi cette limite ?", fontWeight = FontWeight.Bold)
            Text("Une application Android ordinaire ne peut pas observer librement tout le trafic des autres applications. Un VPN local demande une autorisation système et doit être conçu avec des limites claires.")
        }
    }
}

@Composable
private fun ReportsScreen(modifier: Modifier, store: LocalPreferences, completed: Int, total: Int) {
    val context = LocalContext.current
    var lastReview by remember { mutableLongStateOf(store.lastReview()) }
    val dateText = if (lastReview == 0L) "Aucun bilan enregistré" else
        SimpleDateFormat("dd/MM/yyyy à HH:mm", Locale.getDefault()).format(Date(lastReview))
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Rapports locaux", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("État du guide", fontWeight = FontWeight.Bold)
                    Text("$completed contrôles confirmés sur $total")
                    Text(dateText, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Button(onClick = {
                        val now = System.currentTimeMillis()
                        store.saveLastReview(now)
                        lastReview = now
                    }) { Text("Enregistrer la date du bilan") }
                }
            }
        }
        item {
            Text("Stockage", fontWeight = FontWeight.Bold)
            Text("Les confirmations et la date du bilan sont conservées dans les préférences privées de l'application sur cet appareil. Aucune transmission réseau n'est prévue.")
        }
    }
}
