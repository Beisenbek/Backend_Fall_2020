case class Todo(id: String, title: Option[String], description:String, done: Boolean)
case class CreateTodo(title: Option[String] , description:String)
