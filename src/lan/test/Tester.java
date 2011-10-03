package lan.test;

import lan.struct.*;

public class Tester {
	public static void main(String[] args) {
		try {
			System.out.print("initing\n");
			System.out.print("creating world\n");
			LanWorld world = new LanWorld("test");
			System.out.print("setting string and integer processor\n");
			world.putProcessor("string", new StringObjectProcessor(world));
			world.putProcessor("integer", new IntegerObjectProcessor(world));
			System.out.print("setting counter processor\n");
			CounterObjectProcessor counterProcessor = new CounterObjectProcessor(world);
			world.putProcessor("counter", counterProcessor);
			System.out.print("creating counter object\n");
			LanInnerObject counterObject = world.createInnerObject();
			System.out.print("setting counter object processor\n");
			counterObject.setProcessor(counterProcessor);
			System.out.print("getting zero integer outer object\n");
			LanObject zeroInteger = world.fromRealObject("integer", 0);
			System.out.print("setting counter object 'count' attribute\n");
			counterObject.setAttribute("count", zeroInteger);
				
			System.out.print("\ninit succeeded\n");
			System.out.print("starting counter processor\n");
			counterProcessor.start();
			
			int i = 0;
			while(i < 100) {
				System.out.print(counterObject.getAttribute("count").getRealObject().toString() + '\n');
				i++;
				Thread.sleep(1000);
			}
			counterProcessor.stop();
			System.out.print("final count:" + counterObject.getAttribute("count").getRealObject().toString() + '\n');
			world.close();
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
}
