package com.example.tasaagaovcps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

/**
 * Bottom sheet with a streaming Gemini chat interface.
 * Displays role-context suggestions and a freeform input field.
 *
 * @param roleName          e.g. "Teacher", "Finance", "Admin"
 * @param onDismiss         Called when the sheet should close
 * @param onSendMessage     Suspending lambda — collect streaming tokens via [onChunk]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiChatBottomSheet(
    roleName: String,
    onDismiss: () -> Unit,
    onSendMessage: suspend (String, (String) -> Unit) -> Unit
) {
    val geminiPurple = Color(0xFF7C4DFF)
    val geminiDark   = Color(0xFF0D001A)

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var inputText by remember { mutableStateOf("") }
    var isStreaming by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val suggestions = when (roleName) {
        "Teacher"     -> listOf("Attendance trend this week?", "Which students need support?", "Generate a class report")
        "Finance"     -> listOf("Fee collection summary", "Top outstanding balances", "Expenses vs budget")
        "Admin"       -> listOf("School overview today", "Draft term newsletter", "Flag any issues")
        "Headteacher" -> listOf("Academic performance overview", "Staff highlights", "Term targets")
        "Parent"      -> listOf("How is my child doing?", "Next fee deadline?", "Any school announcements?")
        "Boarding"    -> listOf("Welfare incidents this week", "Dorm capacity status", "Upcoming events")
        else          -> listOf("Ask me anything about the school", "Get a daily summary")
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || isStreaming) return
        messages.add(ChatMessage(text, isUser = true))
        inputText = ""
        isStreaming = true
        val aiMessageIndex = messages.size
        messages.add(ChatMessage("", isUser = false))

        scope.launch {
            onSendMessage(text) { chunk ->
                val current = messages[aiMessageIndex]
                messages[aiMessageIndex] = current.copy(text = current.text + chunk)
            }
            isStreaming = false
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = geminiDark,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = geminiPurple,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "Ask AI — $roleName View",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Suggestion chips (shown only before first message)
            if (messages.isEmpty()) {
                Text(
                    text = "Suggested questions:",
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                suggestions.forEach { suggestion ->
                    SuggestionChip(
                        onClick = { sendMessage(suggestion) },
                        label = { Text(suggestion, fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFF2D1B69),
                            labelColor = Color(0xFFCE93D8)
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            // Messages list
            if (messages.isNotEmpty()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message = message, accentColor = geminiPurple)
                    }
                    if (isStreaming && messages.lastOrNull()?.text?.isEmpty() == true) {
                        item {
                            Row(
                                modifier = Modifier.padding(start = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    color = geminiPurple,
                                    strokeWidth = 2.dp
                                )
                                Text("Thinking…", color = Color(0xFF9E9E9E), fontSize = 12.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Input row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask about attendance, fees, students…", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    enabled = !isStreaming,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { sendMessage(inputText) }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = geminiPurple,
                        cursorColor = geminiPurple,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                IconButton(
                    onClick = { sendMessage(inputText) },
                    enabled = inputText.isNotBlank() && !isStreaming,
                    modifier = Modifier
                        .size(48.dp)
                        .background(geminiPurple, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage, accentColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!message.isUser) {
            Icon(
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(18.dp).padding(top = 4.dp)
            )
            Spacer(Modifier.width(4.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (message.isUser) accentColor else Color(0xFF2D1B69),
                    shape = RoundedCornerShape(
                        topStart = if (message.isUser) 16.dp else 4.dp,
                        topEnd = if (message.isUser) 4.dp else 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text.ifEmpty { "…" },
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }

        if (message.isUser) {
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(18.dp).padding(top = 4.dp)
            )
        }
    }
}
