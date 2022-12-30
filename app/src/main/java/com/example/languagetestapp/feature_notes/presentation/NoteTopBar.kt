package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NoteTopBar(
    state: NoteActivityState,
    onTextChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSearchClick: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onNavClicked: () -> Unit
) {
    
    when (state.searchWidgetState) {
        is SearchWidgetState.Closed -> {
            DefaultTopBar(
                onSearchClick = { onSearchTriggered() },
                onNavIconClick = { onNavClicked() }
            )
        }
        is SearchWidgetState.Opened -> {
            
        }
    }
}

@Composable
fun DefaultTopBar(
    onSearchClick: () -> Unit,
    onNavIconClick: () -> Unit
) {

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onNavIconClick()
            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Open drawer")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        title = {
            Text(text = "Notes") // todo later change title depending on current Screen
        }
    )
}

@Composable
fun SearchTopBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onCloseClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Search",
                    color = Color.White
                )
            },
            textStyle = TextStyle(fontSize = MaterialTheme.typography.subtitle1.fontSize),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    onClick = { /*todo*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClick()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchClick(text) }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
    }
}

sealed class SearchWidgetState {
    object Opened: SearchWidgetState()
    object Closed: SearchWidgetState()
}