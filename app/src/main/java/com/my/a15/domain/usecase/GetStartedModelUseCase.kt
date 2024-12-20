package com.my.a15.domain.usecase

import com.my.a15.domain.MyModelNum
import com.my.a15.domain.RepositoryGame
import javax.inject.Inject

class GetStartedModelUseCase@Inject constructor(private val repositoryGameImpl: RepositoryGame) {
    operator fun invoke(): MyModelNum {
        return repositoryGameImpl.getStartGameModel()
    }
}