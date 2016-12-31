cd coin-liquidity
git pull
mvn clean package
cd ..

systemctl stop coin-liquidity.service
rm coin-liquidity.jar
cp coin-liquidity/coin-liquidity-web/target/coin-liquidity-web-0.1-SNAPSHOT.jar coin-liquidity.jar
systemctl start coin-liquidity.service