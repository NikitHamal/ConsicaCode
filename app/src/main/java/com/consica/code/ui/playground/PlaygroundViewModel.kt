package com.consica.code.ui.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.consica.code.ConsicaCodeApp
import com.consica.code.data.local.entity.CodeHistoryEntity
import com.consica.code.data.model.*
import com.consica.code.data.repository.AppRepository
import com.consica.code.domain.PythonRunner
import com.consica.code.domain.HtmlRenderer
import com.consica.code.domain.CharacterGuide
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlaygroundViewModel : ViewModel() {
    private val app = ConsicaCodeApp.instance
    private val repository = AppRepository(app.database, app.dataStore)
    private val pythonRunner = PythonRunner(app)
    private val htmlRenderer = HtmlRenderer(app)
    private val guide = CharacterGuide()

    val userProfile: Flow<UserProfile> = repository.userProfile

    private val _codeFieldValue = MutableStateFlow(
        androidx.compose.ui.text.input.TextFieldValue("")
    )
    val codeFieldValue: StateFlow<androidx.compose.ui.text.input.TextFieldValue> = _codeFieldValue.asStateFlow()

    private val _output = MutableStateFlow("")
    val output: StateFlow<String> = _output.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _showOutput = MutableStateFlow(false)
    val showOutput: StateFlow<Boolean> = _showOutput.asStateFlow()

    private val _currentLanguage = MutableStateFlow(CodeLanguage.HTML)
    val currentLanguage: StateFlow<CodeLanguage> = _currentLanguage.asStateFlow()

    private val _showHint = MutableStateFlow(true)
    val showHint: StateFlow<Boolean> = _showHint.asStateFlow()

    fun updateCode(value: androidx.compose.ui.text.input.TextFieldValue) {
        _codeFieldValue.value = value
    }

    fun setLanguage(language: CodeLanguage) {
        _currentLanguage.value = language
    }

    fun toggleHint() {
        _showHint.update { !it }
    }

    fun clearOutput() {
        _showOutput.value = false
        _output.value = ""
    }

    fun runCode(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _isRunning.value = true
            _showOutput.value = true
            val code = _codeFieldValue.value.text

            val result = when (_currentLanguage.value) {
                CodeLanguage.PYTHON -> {
                    val pr = pythonRunner.runPython(code)
                    pr.output + if (pr.error != null) "\n⚠️ ${pr.error}" else ""
                }
                CodeLanguage.HTML -> {
                    val hr = htmlRenderer.renderHtml(code)
                    "HTML rendered successfully!\nPreview contains ${code.lines().size} lines."
                }
                else -> "[Output for ${_currentLanguage.value.displayName}]"
            }

            _output.value = result
            val isSuccess = !result.contains("Error") && !result.contains("error")

            // Save code history
            repository.saveCodeHistory(
                CodeHistoryEntity(
                    language = _currentLanguage.value.key,
                    codeContent = code,
                    output = result,
                    isSuccess = isSuccess
                )
            )

            _isRunning.value = false
            delay(300)
            onSuccess("seed_001")
        }
    }

    fun loadLesson(lessonId: String) {
        viewModelScope.launch {
            val lesson = LessonLibrary.getSeedLesson()
            if (lesson.id == lessonId) {
                _codeFieldValue.value = androidx.compose.ui.text.input.TextFieldValue(
                    lesson.codeChallenge.starterCode
                )
                _currentLanguage.value = lesson.codeChallenge.language
            }
        }
    }
}