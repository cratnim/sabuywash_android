package com.example.testintegrateui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testintegrateui.ui.theme.TestIntegrateUITheme

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class MainActivity : ComponentActivity() {
    private val SSO_CHANNEL = "com.smilewash.app/sso"
    private val FLUTTER_ENGINE_ID = "my_flutter_engine"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestIntegrateUITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onOpenFlutter = {
                            startFlutterWithArguments()
                        }
                    )
                }
            }
        }
    }

    private fun startFlutterWithArguments() {
        val currentFlavor = BuildConfig.FLAVOR
        android.util.Log.d("FlavorCheck", "Android Native Flavor: $currentFlavor")
        val flutterEngine = FlutterEngine(this)

        setupSSOMethodChannel(flutterEngine)

        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault(),
            listOf(currentFlavor)
        )

        FlutterEngineCache.getInstance().put(FLUTTER_ENGINE_ID, flutterEngine)

        startActivity(
            FlutterActivity
                .withCachedEngine(FLUTTER_ENGINE_ID)
                .build(this)
        )
    }

    private fun setupSSOMethodChannel(flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, SSO_CHANNEL)
            .setMethodCallHandler { call, result ->
                if (call.method == "getAuthentikToken") {
                    handleGetToken(result)
                } else {
                    result.notImplemented()
                }
            }
    }

    private fun handleGetToken(result: MethodChannel.Result) {
        val latestToken = "latest_authentik_token"
        result.success(mapOf("authentik_token" to latestToken))
    }

    override fun onDestroy() {
        super.onDestroy()
        FlutterEngineCache.getInstance().remove(FLUTTER_ENGINE_ID)
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, onOpenFlutter: () -> Unit = {}) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello Android!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onOpenFlutter) {
            Text("Open Flutter Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TestIntegrateUITheme {
        MainScreen()
    }
}