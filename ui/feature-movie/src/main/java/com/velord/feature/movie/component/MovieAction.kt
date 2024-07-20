package com.velord.feature.movie.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.velord.uicore.compose.preview.PreviewCombined

@Composable
internal fun BoxScope.MovieAction(
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExtendedFloatingActionButton(
            onClick = onSortClick,
            modifier = Modifier,
            text = {
                Text(stringResource(id = com.velord.resource.R.string.sort_by))
            },
            icon = {
                Icon(
                    Icons.Filled.Sort,
                    "Floating action button sort",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        )
        ExtendedFloatingActionButton(
            onClick = onFilterClick,
            modifier = Modifier,
            text = {
                Text(stringResource(id = com.velord.resource.R.string.filter_by))
            },
            icon = {
                Icon(
                    Icons.Filled.Filter,
                    "Floating action button filter",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        )
    }
}

@PreviewCombined
@Composable
private fun Preview() {
    Box(modifier = Modifier.fillMaxSize()) {
        MovieAction(
            onSortClick = {},
            onFilterClick = {}
        )
    }
}