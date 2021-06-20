# Trading-bot
This project is auto trading system for cryptocurrency.   
**Investors are responsible for all investments.**

### How to use
This system based on Bithumb API.
So, It is necessary to create Bithumb API(include trading service) on Bithumb web site(https://www.bithumb.com/api_support/management_api).   
Since create your API key, to sign in this system, please type in your Connect key(ID) and Secret key(password).

![로그인](https://user-images.githubusercontent.com/23163982/122660841-9002e680-d1bf-11eb-96c6-4e803cb2e40a.PNG)

When you sign in first time, the auto trading button is disabled.   
It is protect your assets. The number of holdings, profits, and average bidding price is limited to those bought in this system.

![메인_초기](https://user-images.githubusercontent.com/23163982/122661119-102a4b80-d1c2-11eb-9b4a-f19fef1874c2.png)

To enable auto trading feature, please initially set your auto trading setting.   
When you move the setting page, you can set your buying and selling conditions.   
Buying condition only use moving average line.   
They buy, when the market price reaches below the user-specified ratio based on the moving average line.   
Selling condition only use profit ratio.   
System also sell,  when the market price reaches over the user-specified ratio based on profit ratio.

![설정](https://user-images.githubusercontent.com/23163982/122661070-824e6080-d1c1-11eb-9053-8b75b3bf279f.PNG).

If you set completely and go to main page, you can see auto trading button is enabled.   
Now, you ready to use this system.

![메인](https://user-images.githubusercontent.com/23163982/122661279-706dbd00-d1c3-11eb-9ebf-9b07a91af73e.png).
