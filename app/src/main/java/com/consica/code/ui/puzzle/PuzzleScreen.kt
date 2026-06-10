package com.consica.code.ui.puzzle

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.consica.code.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow
import com.consica.code.ui.components.TerraDialogueBubble
import com.consica.code.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleScreen(
    onBack: () -> Unit,
    viewModel: PuzzleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val puzzleState by viewModel.puzzleState.collectAsState(initial = PuzzleUiState())
    val draggedIndex by viewModel.draggedIndex.collectAsState(initial = -1)
    val isChecking by viewModel.isChecking.collectAsState(initial = false)
    val result by viewModel.result.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B5E20).copy(alpha = 0.04f),
                        LeafLight,
                        CleanWhite
                    )
                )
            )
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.puzzle_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Puzzle info
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = ForestGreenContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🧩", fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Arrange the HTML",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SoilDark
                        )
                        Text(
                            text = stringResource(R.string.puzzle_drag_hint),
                            style = MaterialTheme.typography.bodySmall,
                            color = StoneGray
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Result feedback
            AnimatedVisibility(visible = result != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CardShape,
                    colors = CardDefaults.cardColors(
                        containerColor = when (result) {
                            "correct" -> Color(0xFFC8E6C9)
                            else -> Color(0xFFFFF3CD)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (result == "correct") "✅" else "🔄",
                            fontSize = 24.sp
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = if (result == "correct")
                                stringResource(R.string.puzzle_correct)
                            else stringResource(R.string.puzzle_try_again),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SoilDark
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Code blocks
            Text(
                text = "Your Code Blocks:",
                style = MaterialTheme.typography.labelMedium,
                color = StoneGray,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(
                    count = puzzleState.currentBlocks.size,
                    key = { puzzleState.currentBlocks[it].id }
                ) { index ->
                    val block = puzzleState.currentBlocks[index]
                    CodeBlockCard(
                        block = block,
                        isDragging = draggedIndex == index,
                        onDragStart = { viewModel.startDragging(index) },
                        onMoveUp = { viewModel.moveBlock(index, -1) },
                        onMoveDown = { viewModel.moveBlock(index, 1) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.resetPuzzle() },
                    modifier = Modifier.weight(1f),
                    shape = PillButtonShape
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.puzzle_reset))
                }
                Button(
                    onClick = { viewModel.checkPuzzle() },
                    modifier = Modifier.weight(1f),
                    shape = PillButtonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    enabled = !isChecking
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        stringResource(R.string.puzzle_check),
                        color = TextOnPrimary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun CodeBlockCard(
    block: PuzzleBlock,
    isDragging: Boolean,
    onDragStart: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isDragging) Modifier.shadow(8.dp, RoundedCornerShape(16.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDragging) RiverBlue.copy(alpha = 0.08f) else CleanWhite
        ),
        border = if (isDragging)
            BorderStroke(2.dp, RiverBlue)
        else
            BorderStroke(1.dp, StoneGrayLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Drag handle
            IconButton(onClick = onDragStart, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.DragIndicator, contentDescription = "Drag", tint = StoneGray)
            }

            // Code content
            Text(
                text = block.code,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .background(
                        Color(0xFF1E1E1E),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                color = Color(0xFF81C784),
                style = androidx.compose.ui.text.TextStyle(lineHeight = 20.sp)
            )

            // Move buttons
            Column {
                IconButton(onClick = onMoveUp, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Move up", tint = StoneGray)
                }
                IconButton(onClick = onMoveDown, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Move down", tint = StoneGray)
                }
            }
        }
    }
}

data class PuzzleBlock(
    val id: String,
    val code: String,
    val correctPosition: Int
)

class PuzzleViewModel : ViewModel() {
    private val defaultBlocks = listOf(
        PuzzleBlock("1", "<h1>Hello World</h1>", 1),
        PuzzleBlock("2", "</body>", 4),
        PuzzleBlock("3", "<html>", 0),
        PuzzleBlock("4", "<body>", 2),
        PuzzleBlock("5", "</html>", 5)
    )

    private val _puzzleState = MutableStateFlow(PuzzleUiState())
    val puzzleState: StateFlow<PuzzleUiState> = _puzzleState.asStateFlow()

    private val _draggedIndex = MutableStateFlow(-1)
    val draggedIndex: StateFlow<Int> = _draggedIndex.asStateFlow()

    private val _isChecking = MutableStateFlow(false)
    val isChecking: StateFlow<Boolean> = _isChecking.asStateFlow()

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result.asStateFlow()

    fun startDragging(index: Int) {
        _draggedIndex.value = index
    }

    fun moveBlock(fromIndex: Int, direction: Int) {
        _puzzleState.update { state ->
            val newBlocks = state.currentBlocks.toMutableList()
            val toIndex = fromIndex + direction
            if (toIndex in newBlocks.indices) {
                val temp = newBlocks[fromIndex]
                newBlocks[fromIndex] = newBlocks[toIndex]
                newBlocks[toIndex] = temp
            }
            state.copy(currentBlocks = newBlocks)
        }
        _result.value = null
    }

    fun checkPuzzle() {
        _isChecking.value = true
        val blocks = _puzzleState.value.currentBlocks
        val isCorrect = blocks.withIndex().all { (index, block) ->
            block.correctPosition == index
        }
        _result.value = if (isCorrect) "correct" else "incorrect"
        _isChecking.value = false
    }

    fun resetPuzzle() {
        _puzzleState.value = PuzzleUiState(currentBlocks = defaultBlocks.shuffled())
        _result.value = null
        _draggedIndex.value = -1
    }
}

data class PuzzleUiState(
    val currentBlocks: List<PuzzleBlock> = listOf(
        PuzzleBlock("1", "<h1>Hello World</h1>", 1),
        PuzzleBlock("2", "</body>", 4),
        PuzzleBlock("3", "<html>", 0),
        PuzzleBlock("4", "<body>", 2),
        PuzzleBlock("5", "</html>", 5)
    ).shuffled()
)