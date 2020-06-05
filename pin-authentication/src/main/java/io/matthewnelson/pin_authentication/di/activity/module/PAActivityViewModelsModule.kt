package io.matthewnelson.pin_authentication.di.activity.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.matthewnelson.pin_authentication.di.activity.PAActivityScope
import io.matthewnelson.pin_authentication.di.activity.PAActivityViewModelKey
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivityViewModel
import io.matthewnelson.pin_authentication.viewmodel.PAActivityViewModelFactory

/**
 * @suppress
 * */
@Module
internal abstract class PAActivityViewModelsModule {

    @PAActivityScope
    @Binds
    abstract fun bindPAActivityViewModelFactory(
        paActivityViewModelFactory: PAActivityViewModelFactory
    ): ViewModelProvider.Factory

    @PAActivityScope
    @Binds
    @IntoMap
    @PAActivityViewModelKey(PinAuthenticationActivityViewModel::class)
    abstract fun bindPinAuthenticationActivityViewModel(
        viewModel: PinAuthenticationActivityViewModel
    ): ViewModel

}