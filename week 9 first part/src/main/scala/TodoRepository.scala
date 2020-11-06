import java.util.UUID


import scala.concurrent.{ExecutionContext, Future}

trait TodoRepository {
  def all(): Future[Seq[Todo]]

  def done(): Future[Seq[Todo]]

  def pending(): Future[Seq[Todo]]

  def create(createTodo:CreateTodo): Future[Todo]
}


class InMemoryTodoRepository(todo:Seq[Todo] = Seq.empty)(implicit  ex:ExecutionContext) extends TodoRepository {
  private var todos: Vector[Todo] = todo.toVector

  override def all(): Future[Seq[Todo]] = Future.successful(todos)

  override def done(): Future[Seq[Todo]] = Future.successful(todos.filter(_.done))

  override def pending(): Future[Seq[Todo]] = Future.successful(todos.filterNot(_.done))

  override def create(createTodo: CreateTodo): Future[Todo] =
    if (createTodo.description.startsWith("e1")) Future.failed {
      new Exception("e1")
    }
    else
      Future.successful {
        val todo = Todo(
          id = UUID.randomUUID().toString,
          title = createTodo.title,
          description = createTodo.description,
          done = false
        )
        todos = todos :+ todo
        todo
      }
}