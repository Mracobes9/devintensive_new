package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when(question){
                Question.NAME -> Question.NAME.question
                Question.PROFESSION -> Question.PROFESSION.question
                Question.MATERIAL -> Question.MATERIAL.question
                Question.BDAY -> Question.BDAY.question
                Question.SERIAL -> Question.SERIAL.question
                Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val (chk, txt) = question.isAnswerValid(answer)
        return if (!chk) {
            "$txt\n${question.question}" to status.color
        } else {
            if (question.answers.contains(answer.toLowerCase()) || question == Question.IDLE) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
            } else {
                status = status.nextStatus()
                if (status == Status.NORMAL) {
                    question = Question.NAME
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                } else {
                    "Это неправильный ответ\n${question.question}" to status.color
                }
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>){
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status{
            return if (this.ordinal < values().lastIndex){
                values()[this.ordinal +1]
            }else{
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>){
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun isAnswerValid(answer: String): Pair<Boolean, String> {
                return if (answer[0].isUpperCase()) {
                    Pair(true, "")
                }else {
                    Pair(false, "Имя должно начинаться с заглавной буквы")
                }
            }
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun isAnswerValid(answer: String): Pair<Boolean, String> {
                return if (answer[0].isUpperCase()) {
                    Pair(false, "Профессия должна начинаться со строчной буквы")
                }else {
                    Pair(true, "")
                }
            }
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun isAnswerValid(answer: String): Pair<Boolean, String> {
                return if (Regex("""\d+""").containsMatchIn(input = answer)) {
                    Pair(false, "Материал не должен содержать цифр")
                }else {
                    Pair(true, "")
                }
            }
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun isAnswerValid(answer: String): Pair<Boolean, String> {
                return if (answer.toIntOrNull() == null) {
                    Pair(false, "Год моего рождения должен содержать только цифры")
                }else {
                    Pair(true, "")
                }
            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun isAnswerValid(answer: String): Pair<Boolean, String> {
                return if (!Regex("""\d{7}""").matches(input = answer)) {
                    Pair(false, "Серийный номер содержит только цифры, и их 7")
                }else {
                    Pair(true, "")
                }
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun isAnswerValid(answer: String): Pair<Boolean, String> {
                return Pair(true, "")
            }
        };

        abstract fun nextQuestion(): Question
        abstract fun isAnswerValid(answer: String): Pair<Boolean, String>
    }
}