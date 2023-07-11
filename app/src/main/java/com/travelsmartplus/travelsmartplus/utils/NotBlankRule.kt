package com.travelsmartplus.travelsmartplus.utils

import com.wajahatkarim3.easyvalidation.core.rules.BaseRule

/**
 * Custom rule
 * Extends EasyValidation Library to check for blank strings
 *
 * @author Gabriel Salas
 */

class NotBlankRule : BaseRule {


    override fun validate(text: String) : Boolean {
        return text.isNotBlank()
    }

    // Add your invalid check message here
    override fun getErrorMessage() : String {
        return "Can't be blank!"
    }

    override fun setError(msg: String) {
        TODO("Not yet implemented")
    }

}