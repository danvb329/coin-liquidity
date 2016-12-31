rm coin-liquidity.jar
cd coin-liquidity
git pull
mvn clean package
cd ..
cp coin-liquidity/coin-liquidity-web/target/coin-liquidity-web-0.1-SNAPSHOT.jar coin-liquidity.jar