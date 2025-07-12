package com.example.controledequebra

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.controledequebra.ui.theme.ControleDeQuebraTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var vibrator: Vibrator
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar vibrator
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        
        // Configurar para esconder barras do sistema
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Configurar fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        
        setContent {
            ControleDeQuebraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ControleDeQuebraApp(
                        onAlertSound = { playAlertSound() },
                        onStopSound = { stopAlertSound() },
                        onVibrate = { vibrate() },
                        onBackPressed = {
                            finish()
                            // Adicionar animação de transição de volta personalizada
                            overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                            )
                        }
                    )
                }
            }
        }
    }
    
    private fun playAlertSound() {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } catch (e: Exception) {
            // Fallback para som simples
            try {
                val tone = android.media.ToneGenerator(android.media.AudioManager.STREAM_ALARM, 100)
                tone.startTone(android.media.ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 2000)
            } catch (ex: Exception) {
                // Ignora se não conseguir tocar som
            }
        }
    }
    
    private fun stopAlertSound() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            // Ignora se não conseguir parar o som
        }
    }
    
    private fun vibrate() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(1000)
            }
        } catch (e: Exception) {
            // Ignora se não conseguir vibrar
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopAlertSound()
    }
}

@Composable
fun ControleDeQuebraApp(
    onAlertSound: () -> Unit,
    onStopSound: () -> Unit,
    onVibrate: () -> Unit,
    onBackPressed: () -> Unit
) {
    var contador by remember { mutableStateOf(0) }
    var isBlinking by remember { mutableStateOf(false) }
    var isSoundPlaying by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    // Animação de piscar
    val blinkAnimation by animateFloatAsState(
        targetValue = if (isBlinking) 0f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink"
    )
    
    // Função para determinar a cor baseada no contador
    fun getAlertColor(): Color {
        return when {
            contador >= 100 || contador <= -100 -> Color(0xFF8B0000).copy(alpha = blinkAnimation) // Vermelho escuro piscando
            contador >= 50 || contador <= -50 -> Color(0xFFDC143C) // Crimson
            contador >= 30 || contador <= -30 -> Color(0xFFFF4500) // Orange Red
            contador >= 20 || contador <= -20 -> Color(0xFFFF6347) // Tomato
            contador >= 10 || contador <= -10 -> Color(0xFFFFA07A) // Light Salmon
            else -> Color.Transparent
        }
    }
    
    // Função para ativar/desativar o piscar e som
    fun updateAlerts() {
        val shouldBlink = contador >= 100 || contador <= -100
        val shouldPlaySound = contador >= 100 || contador <= -100
        
        if (shouldBlink && !isBlinking) {
            isBlinking = true
        } else if (!shouldBlink && isBlinking) {
            isBlinking = false
        }
        
        if (shouldPlaySound && !isSoundPlaying) {
            isSoundPlaying = true
            onAlertSound()
            onVibrate()
        } else if (!shouldPlaySound && isSoundPlaying) {
            isSoundPlaying = false
            onStopSound()
        }
    }
    
    // Atualizar alertas quando o contador muda
    LaunchedEffect(contador) {
        updateAlerts()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getAlertColor())
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            // Coluna da esquerda - Botões de controle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Botão Menos com área de clique aumentada
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clickable {
                            contador--
                            scope.launch {
                                // Feedback tátil (vibração)
                                // HapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                        .background(
                            color = Color(0xFFE53E3E),
                            shape = RoundedCornerShape(16.dp) // Adicionado raio para bordas arredondadas
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "-",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Text(
                    text = "DIMINUIR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            // Coluna central - Display do contador
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .size(230.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contador.toString(),
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = when {
                                contador >= 100 || contador <= -100 -> Color(0xFF8B0000) // Vermelho escuro
                                contador >= 50 || contador <= -50 -> Color(0xFFDC143C) // Crimson
                                contador >= 30 || contador <= -30 -> Color(0xFFFF4500) // Orange Red
                                contador >= 20 || contador <= -20 -> Color(0xFFFF6347) // Tomato
                                contador >= 10 || contador <= -10 -> Color(0xFFFF8C00) // Dark Orange
                                else -> Color.Black
                            }
                        )
                    }
                }
            }
            
            // Coluna da direita - Botão Mais
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Botão Mais com área de clique aumentada
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clickable {
                            contador++
                            scope.launch {
                                // Feedback tátil (vibração)
                                // HapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                        .background(
                            color = Color(0xFF2E8B57),
                            shape = RoundedCornerShape(16.dp) // Adicionado raio para bordas arredondadas
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Text(
                    text = "AUMENTAR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        
        // Sistema de clique por região da tela
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { }
        ) {
            // Área da esquerda (diminuir)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.4f)
                    .clickable {
                        contador--
                        scope.launch {
                            // Feedback tátil (vibração)
                            // HapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
            )
            
            // Área da direita (aumentar)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.4f)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        contador++
                        scope.launch {
                            // Feedback tátil (vibração)
                            // HapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
            )
        }
        
        // Indicador de nível de alerta na parte inferior
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlertLevelIndicator(contador = contador)
            
            // Botão de Reset com área de clique aumentada
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        contador = 0
                        scope.launch {
                            // Feedback tátil (vibração)
                            // HapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
                    .background(
                        color = Color(0xFF4682B4),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Resetar contador",
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }
        }
        
        // Botão de voltar (declarado por último para ficar no topo)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(56.dp)
                .clickable { onBackPressed() }
                .background(
                    color = Color.White.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                modifier = Modifier.size(28.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun AlertLevelIndicator(contador: Int, modifier: Modifier = Modifier) {
    val alertLevel = when {
        contador >= 100 || contador <= -100 -> "ALERTA MÁXIMO!"
        contador >= 50 || contador <= -50 -> "Muito Grave"
        contador >= 30 || contador <= -30 -> "Mais Grave"
        contador >= 20 || contador <= -20 -> "Leve"
        contador >= 10 || contador <= -10 -> "Atenção"
        else -> "Normal"
    }
    
    val alertColor = when {
        contador >= 100 || contador <= -100 -> Color(0xFF8B0000) // Vermelho escuro
        contador >= 50 || contador <= -50 -> Color(0xFFDC143C) // Crimson
        contador >= 30 || contador <= -30 -> Color(0xFFFF4500) // Orange Red
        contador >= 20 || contador <= -20 -> Color(0xFFFF6347) // Tomato
        contador >= 10 || contador <= -10 -> Color(0xFFFFA07A) // Light Salmon
        else -> Color.White
    }
    
    val textColor = when {
        contador >= 10 || contador <= -10 -> Color.White
        else -> Color.Black
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        colors = CardDefaults.cardColors(containerColor = alertColor.copy(alpha = 0.9f))
    ) {
        Text(
            text = alertLevel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ControleDeQuebraAppPreview() {
    ControleDeQuebraTheme {
        ControleDeQuebraApp(
            onAlertSound = {},
            onStopSound = {},
            onVibrate = {},
            onBackPressed = {}
        )
    }
}