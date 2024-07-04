package com.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.myapplication.ui.theme.MyApplicationTheme
import com.myapplication.ui.theme.Purple40


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                TodoApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp() {
    var todos by remember { mutableStateOf(listOf<TodoItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var newTodoText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Todo",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center))
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,

                    )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                todos.forEach { todo ->
                    TodoRow(todo) {
                        todos = todos.map { if (it == todo) it.copy(isDone = !it.isDone) else it }
                    }
                }
            }
        }
    )

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Add Todo", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = newTodoText,
                        onValueChange = { newTodoText = it },
                        label = { Text("Todo") },
                        trailingIcon = {
                            if (newTodoText.isNotEmpty()) {
                                IconButton(onClick = { newTodoText = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        },
                        isError = showError
                    )
                    if (showError) {
                        Text(
                            text = "Todo cannot be empty",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = {
                            newTodoText = ""
                            showDialog = false
                            showError = false
                        }) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            if (newTodoText.isNotEmpty()) {
                                todos = todos + TodoItem(newTodoText, false)
                                newTodoText = ""
                                showDialog = false
                                showError = false
                            } else {
                                showError = true
                            }
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoRow(todo: TodoItem, onCheckChange: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(todo.text)
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { onCheckChange() }
        )
    }
}

data class TodoItem(val text: String, val isDone: Boolean)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        TodoApp()
    }
}