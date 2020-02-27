import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

	public static void main(String[] args) {

		List<Product> productList = Arrays.asList(
				new Product("Health", "Aspirin", 3.50f, 2),
				new Product("Second Hand", "Pullover", 10.99f, 1),
				new Product("Food", "Bread", 0.99f, 1),
				new Product("Food", "Milk", 1.50f, 3),
				new Product("Food", "Eggs", 0.1f, 10)
		);

		IsHealthPredicate isHealth = category -> !"Health".equals(category.getCategory());

		IsSHPredicate isSecondHand = category -> !"Second Hand".equals(category.getCategory());

		VATFunction valueAddedTax = vat -> {
			return new Product(vat.getCategory(), vat.getName(), vat.getPrice() * 1.18f, vat.getQuantity());
		};

		ConsolePrintConsumer printer = System.out::println;

		/*
		 * Что подразумевают под "терминальные" операции в потоке?
		 * Терминальные (terminal) операции в потоке подразумевают конец работы
		 * с потоком. В потоке может быть только одна терминальная операция,
		 * которая ставится в конце потока. А вот промежуточных (intermediate)
		 * операций может быть много
		 */
		
		List<Product> newProductList = productList.stream()
				.filter(isHealth::test)  // intermediate. Записывает в поток только то, что соотвествует какому-то условию
				.filter(isSecondHand::test)  // intermediate
				.map(valueAddedTax::calculate)  // intermediate.  Выполняет какие-то операции, изменяя каждый элемент
				.collect(Collectors.toList());  // terminal. Преобразуем поток в List

		newProductList.stream().forEach(printer::consume);

	}

}

// Интерфейс Function содержит метод, который принимает одно значение и
// возвращает другое
@FunctionalInterface
interface VATFunction {
	Product calculate(Product product);
}

// Интерфейс Predicate содержит метод, который проверяет какое-то условие и возвращает true или false
@FunctionalInterface
interface IsHealthPredicate {
	Boolean test(Product product);
}

@FunctionalInterface
interface IsSHPredicate {
	Boolean test(Product product);
}

// Интерфейс Consumer содержит метод, который что-то делает, но ничего не возвращает
@FunctionalInterface
interface ConsolePrintConsumer {
	void consume(Product product);
}