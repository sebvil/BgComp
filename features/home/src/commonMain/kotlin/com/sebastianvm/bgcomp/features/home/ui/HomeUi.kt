package com.sebastianvm.bgcomp.features.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sebastianvm.bgcomp.designsys.components.Button
import com.sebastianvm.bgcomp.designsys.components.Scaffold
import com.sebastianvm.bgcomp.features.home.viewmodel.GameClicked
import com.sebastianvm.bgcomp.features.home.viewmodel.HomeState
import com.sebastianvm.bgcomp.features.home.viewmodel.HomeUserAction
import com.sebastianvm.bgcomp.mvvm.Ui
import com.sebastianvm.bgcomp.ui.UiString

object HomeUi : Ui<HomeState, HomeUserAction> {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun invoke(state: HomeState, handle: (HomeUserAction) -> Unit, modifier: Modifier) {
        Scaffold(
            modifier = modifier.fillMaxSize().padding(vertical = 16.dp),
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(state.games) {
                    Button(
                        text = UiString(it.displayName),
                        onClick = {
                            handle(GameClicked(it))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
