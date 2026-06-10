package com.consica.code.ui.workspace

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.consica.code.R
import com.consica.code.data.local.entity.WorkspaceEntity
import com.consica.code.data.repository.AppRepository
import com.consica.code.ConsicaCodeApp
import com.consica.code.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreen(
    onOpenPlayground: (Long) -> Unit,
    onBack: () -> Unit
) {
    val app = remember { ConsicaCodeApp.instance }
    val repository = remember { AppRepository(app.database, app.dataStore) }
    val workspaces by repository.observeWorkspaces().collectAsState(initial = emptyList())

    var showNewProjectDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LeafLight)
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.workspace_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // New project button
            Button(
                onClick = { showNewProjectDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = PillButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.workspace_new_project), color = TextOnPrimary)
            }

            Spacer(Modifier.height(20.dp))

            if (workspaces.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📁", fontSize = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.workspace_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = StoneGray
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.workspace_my_projects),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SoilDark
                )
                Spacer(Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(workspaces) { workspace ->
                        WorkspaceCard(
                            workspace = workspace,
                            onClick = { onOpenPlayground(workspace.id) },
                            onDelete = {
                                scope.launch { repository.deleteWorkspace(workspace.id) }
                            }
                        )
                    }
                }
            }
        }
    }

    // New project dialog
    if (showNewProjectDialog) {
        AlertDialog(
            onDismissRequest = { showNewProjectDialog = false },
            title = { Text("New Project") },
            text = {
                OutlinedTextField(
                    value = newProjectName,
                    onValueChange = { newProjectName = it },
                    label = { Text("Project Name") },
                    placeholder = { Text("My Awesome Project") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = InputShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForestGreen,
                        unfocusedBorderColor = StoneGrayLight
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newProjectName.isNotBlank()) {
                            scope.launch {
                                repository.saveWorkspace(
                                    WorkspaceEntity(
                                        name = newProjectName.trim(),
                                        language = "html"
                                    )
                                )
                            }
                            showNewProjectDialog = false
                            newProjectName = ""
                        }
                    }
                ) {
                    Text("Create", color = ForestGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewProjectDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = DialogShape,
            containerColor = CleanWhite
        )
    }
}

@Composable
fun WorkspaceCard(
    workspace: WorkspaceEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CleanWhite)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (workspace.language) {
                    "python" -> Icons.Filled.Code
                    else -> Icons.Filled.Html
                },
                contentDescription = null,
                tint = ForestGreen,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workspace.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SoilDark
                )
                Text(
                    text = workspace.language.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = StoneGray
                )
                Text(
                    text = java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.ENGLISH)
                        .format(java.util.Date(workspace.updatedAt)),
                    style = MaterialTheme.typography.labelSmall,
                    color = StoneGray.copy(alpha = 0.7f)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = StoneGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}