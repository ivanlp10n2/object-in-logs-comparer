package sandbox

class NonCats {

  def completeLine: List[Int] => Either[String, Int]= {
    _.zipWithIndex
      .foldLeft(Right(0): Either[String, Int]){
        case (acc, (n, index)) => {
          val stepsForward = n - (index + 1) // 1, 2, 4, 3 = 0, 0, 1, -1

          if (stepsForward > 2) {
            Left("Too chaotic")
          } else {
            if (stepsForward > 0)
              acc.map(_ + stepsForward)
            else acc
          }
        }
      }
//      .map(_.filter(_ > 0).sum)
  }

  def foor : List[Int] => Unit = {
    _.zipWithIndex
      .foldLeft(0) {
        case (acc, (n, index)) => {
          val stepsForward = n - (index + 1) // 1, 2, 4, 3 = 0, 0, 1, -1

          if (stepsForward > 2) {
            println("Too chaotic")
          }

          if (stepsForward > 0) {
            acc + stepsForward
          } else acc
        }
      }
//      .map(_.filter(_ > 0).sum)
  }
}
