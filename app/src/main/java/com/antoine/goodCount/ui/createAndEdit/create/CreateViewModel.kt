package com.antoine.goodCount.ui.createAndEdit.create

import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.CommonPotRepository
import com.google.android.gms.tasks.Task

class CreateViewModel: ViewModel() {

    fun createCommonPot(commonPot: CommonPot, participant: Participant): Task<Void> {
        return CommonPotRepository().createCommonPot(commonPot, participant)
    }
}