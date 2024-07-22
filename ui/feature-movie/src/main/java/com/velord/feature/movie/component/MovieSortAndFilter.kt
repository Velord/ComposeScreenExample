package com.velord.feature.movie.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import com.velord.feature.movie.model.MovieFilterOptionUI
import com.velord.feature.movie.model.MovieSortOptionUI
import com.velord.feature.movie.viewModel.MovieUiState
import com.velord.resource.R
import com.velord.uicore.compose.preview.PreviewCombined
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun MovieSortAndFilter(
    uiState: MovieUiState,
    isSortShowing: Boolean,
    isFilterShowing: Boolean,
    isInfoShowing: Boolean,
    onHideSort: () -> Unit,
    onHideFilter: () -> Unit,
    onHideInfo: () -> Unit,
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
    if (isInfoShowing) {
        Info(
            isShowing = isInfoShowing,
            onHide = onHideInfo
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

@Composable
private fun Info(
    isShowing: Boolean,
    onHide: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    Sheet(
        isShowing = isShowing,
        onHide = { scope.hide(onHide) },
        content = {
            Text(
                text = stringResource(id = R.string.info_description_movie),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .verticalScroll(rememberScrollState())
            )
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
        isSortShowing = false,
        isFilterShowing = false,
        isInfoShowing = false,
        onHideSort = {},
        onHideFilter = {},
        onHideInfo = {},
        onSortOptionClick = {},
        onFilterOptionClick = {}
    )
}