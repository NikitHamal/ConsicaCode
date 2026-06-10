package com.consica.code.ui.screens

import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.consica.code.R
import com.consica.code.ui.components.TerraDialogue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreenCodePlaygroundScreen(
    lessonId: String,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val defaultCode = stringResource(R.string.playground_default_code)
    var codeText by remember { mutableStateOf(defaultCode) }
    var outputText by remember { mutableStateOf("") }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.playground_title)) },
                actions = {
                    Button(
                        onClick = {
                            // Inject code into the webview's basic python evaluator
                            val escapedCode = codeText.replace("\n", "\\n").replace("'", "\\'")
                            webViewRef?.evaluateJavascript("evaluatePython('$escapedCode')") { _ ->
                                // Optional callback handling
                            }
                        },
                        modifier = Modifier.padding(end = 16.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(stringResource(R.string.editor_run_beginner))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TerraDialogue(message = stringResource(R.string.playground_instruction))

            // Editor Area
            OutlinedTextField(
                value = codeText,
                onValueChange = { codeText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge, // Monospace normally
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            )

            // Console Output / Web View for Evaluation
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(stringResource(R.string.playground_output_label), modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.labelMedium)

                    // Offline WebView Engine
                    AndroidView(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true
                                settings.allowFileAccess = true

                                webChromeClient = object : WebChromeClient() {
                                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                                        consoleMessage?.message()?.let { msg ->
                                            if(msg.startsWith("EVAL_RESULT:")) {
                                                outputText = msg.removePrefix("EVAL_RESULT:")
                                                if (outputText.contains("Hello Forest!")) {
                                                    onSuccess()
                                                }
                                            } else {
                                                outputText += "\n$msg"
                                            }
                                        }
                                        return true
                                    }
                                }
                                loadUrl("file:///android_asset/python/index.html")
                                webViewRef = this
                            }
                        }
                    )
                }
            }
        }
    }
}
