package com.example.controledequebra

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.controledequebra.ui.theme.ControleDeQuebraTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configurar para esconder barras do sistema
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            ControleDeQuebraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        onControleDeQuebraClick = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            // Adicionar animação de transição personalizada
                            overridePendingTransition(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onControleDeQuebraClick: () -> Unit) {
    var isButtonPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    // Animação de escala para o botão
    val scale by animateFloatAsState(
        targetValue = if (isButtonPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "scale"
    )

    // Animação de pulso contínua
    val pulseAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    // Animação de rotação para elementos decorativos
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Animação de entrada para os elementos
    val titleAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300),
        label = "titleAlpha"
    )
    
    val subtitleAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 600),
        label = "subtitleAlpha"
    )
    
    val buttonAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 900),
        label = "buttonAlpha"
    )
    
    val cardAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 1200),
        label = "cardAlpha"
    )
    
    // Animação de flutuação para elementos decorativos
    val floatAnimation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    // Animação de partículas
    val particleAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle"
    )

    val auroraColor1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF8B5CF6).copy(alpha = 0.3f),
        targetValue = Color(0xFF3B82F6).copy(alpha = 0.3f),
        animationSpec = infiniteRepeatable(tween(7000, easing = EaseInOut), repeatMode = RepeatMode.Reverse),
        label = "aurora1"
    )
    val auroraColor2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF10B981).copy(alpha = 0.3f),
        targetValue = Color(0xFFF59E0B).copy(alpha = 0.3f),
        animationSpec = infiniteRepeatable(tween(9000, easing = EaseInOut), repeatMode = RepeatMode.Reverse),
        label = "aurora2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)), // Fundo sólido escuro
        contentAlignment = Alignment.Center
    ) {
        // Efeito Aurora Borealis no fundo
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(400.dp)
                    .offset(x = (-100).dp, y = (-150).dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(auroraColor1, Color.Transparent)
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(400.dp)
                    .offset(x = 100.dp, y = 150.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(auroraColor2, Color.Transparent)
                        )
                    )
            )
        }

        // Conteúdo principal
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            // Título do app
            Text(
                text = "Rede Plus",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = titleAlpha),
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .graphicsLayer(alpha = titleAlpha)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo com animação de entrada
            Text(
                text = "Sistema de controle de Quebra de caixa.",
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = subtitleAlpha * 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botão principal com animação e efeitos visuais
            Button(
                onClick = {
                    if (!isButtonPressed) {
                        isButtonPressed = true
                        scope.launch {
                            delay(150)
                            onControleDeQuebraClick()
                            isButtonPressed = false
                        }
                    }
                },
                modifier = Modifier
                    .scale(scale * pulseAnimation)
                    .shadow(elevation = 20.dp, spotColor = Color(0xFF10B981), ambientColor = Color(0xFF10B981), shape = CircleShape)
                    .graphicsLayer(alpha = buttonAlpha),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF10B981), // Verde
                                    Color(0xFF059669)   // Verde escuro
                                )
                            )
                        )
                        .padding(horizontal = 48.dp, vertical = 24.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Iniciar",
                            tint = Color.White
                        )
                        Text(
                            text = "INICIAR",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            // Card de funcionalidades com efeito de vidro
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = cardAlpha
                        translationY = (1f - cardAlpha) * 50f
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f) // Efeito vidro
                ),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Funcionalidades",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Layout de uma coluna para melhor alinhamento
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        FeatureItem("📊 Controle de contadores", cardAlpha)
                        FeatureItem("🔔 Alertas Sonoros", cardAlpha)
                        FeatureItem("📱 Feedback Tátil", cardAlpha)
                        FeatureItem("🔄 Modo Landscape", cardAlpha)
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureItem(text: String, alpha: Float) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        listOf(Color(0xFF10B981), Color(0xFF3B82F6))
                    ),
                    shape = CircleShape
                )
        )

        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White.copy(alpha = alpha * 0.9f),
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ControleDeQuebraTheme {
        HomeScreen(
            onControleDeQuebraClick = {}
        )
    }
} 