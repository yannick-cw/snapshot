package io.github.yannick_cw

object Messages {

  private def createMsg(question: String, testName: String, blockName: String, className: String): String =
    s"""${Console.GREEN}
       |$className:
       |$testName
       |$blockName
       |$question
       |${Console.RESET}""".stripMargin

  private def tabNewlines: String => String = _.flatMap(c => if (c == '\n') "\n    " else c.toString)

  def updateQuestion(left: String, right: String, className: String): String =
    createMsg(
      s"""${Console.RED}    ${tabNewlines(left)}
             |  was not equal to
             |    $right
             |  Should it be updated [y/n]?""".stripMargin,
      "testName",
      "- test description",
      className
    )

  def createQuestion(left: String, className: String): String =
    createMsg(
      s"""${Console.YELLOW}  Should a snapshot with value
             |    ${tabNewlines(left)}
             |  be created [y/n]?""".stripMargin,
      "testName",
      "- test description",
      className
    )
}
