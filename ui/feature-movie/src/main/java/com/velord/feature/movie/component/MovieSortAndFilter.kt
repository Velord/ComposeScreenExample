package com.velord.feature.movie.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.velord.feature.movie.model.MovieFilterOptionUI
import com.velord.feature.movie.model.MovieSortOptionUI
import com.velord.feature.movie.viewModel.MovieUiState
import com.velord.uicore.compose.preview.PreviewCombined
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MovieSortAndFilter(
    uiState: MovieUiState,
    isSortShowing: Boolean,
    isFilterShowing: Boolean,
    onHideSort: () -> Unit,
    onHideFilter: () -> Unit,
    onSortOptionClick: (MovieSortOptionUI) -> Unit,
    onFilterOptionClick: (MovieFilterOptionUI) -> Unit
) {
    if (isSortShowing) {
        Sort(
            optionRoster = uiState.sortOptionRoster,
            isShowing = isSortShowing,
            onHide = onHideSort,
            onOptionClick = {
                onSortOptionClick(it)
            }
        )
    }
    if (isFilterShowing) {
        Filter(
            optionRoster = uiState.movieFilterOptionRoster,
            isShowing = isFilterShowing,
            onHide = onHideFilter,
            onOptionClick = {
                onFilterOptionClick(it)
            }
        )
    }
}

private fun CoroutineScope.hide(f: () -> Unit) {
    launch { f() }.invokeOnCompletion { f() }
}

@Composable
private fun Sort(
    optionRoster: List<MovieSortOptionUI>,
    isShowing: Boolean,
    onHide: () -> Unit,
    onOptionClick: (MovieSortOptionUI) -> Unit
) {
    val scope = rememberCoroutineScope()

    Sheet(
        isShowing = isShowing,
        onHide = { scope.hide(onHide) },
        content = {
            optionRoster.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.hide(onHide)
                            onOptionClick(option)
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = option.isSelected,
                        onClick = {
                            scope.hide(onHide)
                            onOptionClick(option)
                        }
                    )
                    Text(
                        text = stringResource(id = option.name),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    )
}

@Composable
private fun Filter(
    optionRoster: List<MovieFilterOptionUI>,
    isShowing: Boolean,
    onHide: () -> Unit,
    onOptionClick: (MovieFilterOptionUI) -> Unit
) {
    val scope = rememberCoroutineScope()

    Sheet(
        isShowing = isShowing,
        onHide = { scope.hide(onHide) },
        content = {
            optionRoster.forEach { option ->
                Button(
                    onClick = { scope.hide(onHide) },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = option.name)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Sheet(
    isShowing: Boolean,
    onHide: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(key1 = isShowing) {
        if (isShowing) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    ModalBottomSheet(onDismissRequest = { onHide() }) {
        Column {
            content()
        }
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    MovieSortAndFilter(
        uiState = MovieUiState.DEFAULT,
        isSortShowing = true,
        isFilterShowing = false,
        onHideSort = {},
        onHideFilter = {},
        onSortOptionClick = {},
        onFilterOptionClick = {}
    )
}