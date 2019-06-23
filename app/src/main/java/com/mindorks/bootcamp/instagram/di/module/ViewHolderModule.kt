package com.mindorks.bootcamp.instagram.di.module

import androidx.lifecycle.LifecycleRegistry
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.di.ViewModelScope
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewHolder
import dagger.Module
import dagger.Provides

@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *>) {

    @Provides
    @ViewModelScope
    fun provideLifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(viewHolder)

    @Provides
    @ViewModelScope
    fun providesGlideHeader(userRepository: UserRepository): Map<String, String> = mutableMapOf(
        (Networking.HEADER_USER_ID to (userRepository.getCurrentUser()?.id ?: "")),
        (Networking.HEADER_ACCESS_TOKEN to (userRepository.getCurrentUser()?.accessToken ?: "")),
        (Networking.HEADER_API_KEY to Networking.API_KEY)
    )
}