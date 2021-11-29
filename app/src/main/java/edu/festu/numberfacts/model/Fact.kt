package edu.festu.numberfacts.model

/**
 * Простая модель факта, который может быть получен из сетевого ресурса.
 * В случае расширения функционала те или иные поля класса могут быть или не быть задействованы.
 * */
data class Fact(var number: Int? = 0,
                var text: String?="",
                var found: Boolean?=false,
                var type: String? = "",
                var date: String? = "No attached date",
                var year: String? = "No attached year")
