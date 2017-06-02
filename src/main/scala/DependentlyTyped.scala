trait Converter[In] {
  type Out
  def apply(in: In): Out
}

object Converter {
  type Aux[In, Out0] = Converter[In] { type Out = Out0 }

  implicit val forA: Converter.Aux[A, String] = new Converter[A] {
    type Out = String
    def apply(in: A): String = in.toString
  }
}

class A {
  def fails[Out](implicit conv: Converter.Aux[A, Out]) = conv(this)
  def works[Out](implicit conv: Converter.Aux[A, Out]): Out = conv(this)
}

object Usage {
  new A().fails.substring(0) //this compiles fine, the problem (typically) only exists in the repl

  /** to reproduce: run `sbt console` and run `new A().fails.substring(0)` twice
    * result: the first time it doesn't compile, on ever consecutive run it compiles fine
    */
}
