#### 一、对称加密

------

###### 1、什么是对称加密？

对称加密就是指，**加密和解密使用同一个密钥的加密方式。**

###### 2、对称加密的工作过程

发送方使用密钥将明文数据加密成密文，然后发送出去，接收方收到密文后，使用同一个密钥将密文解密成明文读取。

###### 3、对称加密的优点

**加密计算量小、速度块，适合对大量数据进行加密的场景。**（记住这个特点，实际使用是会用到的）

###### 4、对称加密的两大不足

- **密钥传输问题**：如上所说，由于对称加密的加密和解密使用的是同一个密钥，所以对称加密的安全性就不仅仅取决于加密算法本身的强度，更取决于密钥是否被安全的保管，因此**加密者如何把密钥安全的传递到解密者手里，就成了对称加密面临的关键问题。**（比如，我们客户端肯定不能直接存储对称加密的密钥，因为被反编译之后，密钥就泄露了，数据安全性就得不到保障，所以**实际中我们一般都是客户端向服务端请求对称加密的密钥，而且密钥还得用非对称加密加密后再传输。**）
- **密钥管理问题**：**再者随着密钥数量的增多，密钥的管理问题会逐渐显现出来。**比如我们在加密用户的信息时，不可能所有用户都用同一个密钥加密解密吧，这样的话，一旦密钥泄漏，就相当于泄露了所有用户的信息，因此需要为每一个用户单独的生成一个密钥并且管理，这样密钥管理的代价也会非常大。



#### 二、AES加密算法

------

###### 1、什么是AES加密算法及AES加密算法的形成过程

###### （1）什么是AES加密算法

上一小节说到了对称加密，那么针对对称加密的话，是有很多的对称加密算法的，如DES加密算法、3DES加密算法等，但是因为AES加密算法的安全性要高于DES和3DES，所以AES已经成为了主要的对称加密算法，因此本篇主要学习一下AES加密算法。

AES加密算法就是众多对称加密算法中的一种，它的英文全称是Advanced Encryption Standard，翻译过来是高级加密标准，它是用来替代之前的DES加密算法的。

**AES加密算法采用分组密码体制，每个分组数据的长度为128位16个字节，密钥长度可以是128位16个字节、192位或256位，一共有四种加密模式，我们通常采用需要初始向量IV的CBC模式，初始向量的长度也是128位16个字节。**

###### （2）AES加密算法的形成过程

- **没人理，呜呜呜，只要有一个人听，我就讲~**

![img](https:////upload-images.jianshu.io/upload_images/1116725-cdd7f13f94441374.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 我每天要处理很多很多的数据，我要把很多神奇的高机密数据加密成枯燥的数据包发给你的WIFI路由器，这一切都是我做的！

![img](https:////upload-images.jianshu.io/upload_images/1116725-b7e093e3ceb7fb72.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 但是，一直以来似乎没有人对我和我的故事感兴趣，呜呜呜......

![img](https:////upload-images.jianshu.io/upload_images/1116725-c2f853916275e0de.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 可其实我的故事要比《灰姑娘》更精彩呢，因为我现在可是分组加密世界的国王。

![img](https:////upload-images.jianshu.io/upload_images/1116725-87d9c7fcf4ec54f8.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 哇，你还没走啊，你是想要听我的故事吗？好吧，让我们荡起双桨......

- **变法风云吹进门，DES称霸武林**

![img](https:////upload-images.jianshu.io/upload_images/1116725-c106081998e3b1da.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 在1975以前，加密界一片混乱，没有人能说出哪种加密算法更好，诸侯争霸，涂炭生灵。

![img](https:////upload-images.jianshu.io/upload_images/1116725-e1bc9a643c1b63ea.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 终于有一天，国家标准局出台了一个活动，说是要号召大家来创建一个好的安全的加密算法，就是选盟主呗。

![img](https:////upload-images.jianshu.io/upload_images/1116725-d69dbbf53e426f18.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 这时一个强有力的竞争者站了出来，它就是IBM，IBM研究出了一套加密算法。

![img](https:////upload-images.jianshu.io/upload_images/1116725-7a3be11666e3e376.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> IBM研究的这一套加密算法在被国家安全局修修改改之后，被国家标准局钦定为了数据加密标准，即DES，因为DES的key更短（key即密钥），S盒更强（S盒是对称加密进行置换计算的基本结构，S盒的强度直接决定了该对称加密算法的好坏）。

![img](https:////upload-images.jianshu.io/upload_images/1116725-56bd8a88f17c6a11.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> DES统治了对称加密世界近20年，学术界也开始认真的研究它，这是第一次，真正的研究它，现代密码学就此诞生。

![img](https:////upload-images.jianshu.io/upload_images/1116725-244d9c08bc015ec8.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 这些年间，许多攻击者挑战着DES，它经历了风风雨雨，而且它真得被打败过几次。

![img](https:////upload-images.jianshu.io/upload_images/1116725-956bf4cb164db237.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 而阻止攻击者的唯一办法就是使用DES加密算法3次，即“Triple-DES”或“3DES”，这种办法可行，但是实在是太慢了。

- **改革春风吹得响，AES要登场**

![img](https:////upload-images.jianshu.io/upload_images/1116725-4e29dcbe7bede114.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 终于，国家标准学会憋不住了，又出台了一个活动，说是号召大家推举一个新的武林盟主，盟主3DES虽然武功厉害，但毕竟太老太慢了。新盟主的条件是武功（加密强度）至少和3DES一样厉害，但是要比3DES快而灵巧。

![img](https:////upload-images.jianshu.io/upload_images/1116725-c89a02afa09cdaff.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 来自五湖四海的朋友们都跃跃欲试。

![img](https:////upload-images.jianshu.io/upload_images/1116725-880bbe5a502483eb.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 我的创造者，Vincent Rijmen和Joan Daemen也在他们之中。他俩把他们俩的名字组合起来，给我起了个名叫Rijndael。

![img](https:////upload-images.jianshu.io/upload_images/1116725-0d149eeb015cb966.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 经过好几轮的pk，大家伙一起投票！

![img](https:////upload-images.jianshu.io/upload_images/1116725-2b01aaa4a9cae2d2.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 最后，我赢了！哦，赢喽，湖人总冠军喽，冠军喽！

![img](https:////upload-images.jianshu.io/upload_images/1116725-6c1108a0ffb0b4fd.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 好了，现在我--AES王朝的序幕已然拉开，跪服吧，我的子民！普天之下，莫非王土；率土之滨，莫非王臣。强如英特尔也不得不向我朝来贺，甚至专门为我量身定制了底层的指令，来让我飞的更高！

![img](https:////upload-images.jianshu.io/upload_images/1116725-950b224a0fcba142.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 啊，吹牛逼的感觉好爽啊！你们可有什么问题？不要让我停下来呀！
>
> A：老师老师，你牛逼吹得这么响，你倒是说说你--AES加密的原理是什么啊！
>
> B：我靠，又要开始了，我走了......
>
> 欸，那位同学不要走......哎，可惜他没有机会看到我的内在了......

###### 2、AES的加密流程

我们先让AES喝口水歇一下，了解一下它的**加密流程**之后，再让它给我们讲它的加密原理。

###### （1）五个关键词讲解

要理解AES的加密流程，会涉及到AES加密的五个关键词，分别是：**分组密码体制**、**Padding**、**密钥**、**初始向量IV**和**四种加密模式**，下面我们一一介绍。

- **分组密码体制**：所谓分组密码体制就是指将明文切成一段一段的来加密，然后再把一段一段的密文拼起来形成最终密文的加密方式。**AES采用分组密码体制，即AES加密会首先把明文切成一段一段的，而且每段数据的长度要求必须是128位16个字节，如果最后一段不够16个字节了，就需要用Padding来把这段数据填满16个字节，然后分别对每段数据进行加密，最后再把每段加密数据拼起来形成最终的密文。**
- **Padding**：**Padding就是用来把不满16个字节的分组数据填满16个字节用的，它有三种模式PKCS5、PKCS7和NOPADDING。**PKCS5是指分组数据缺少几个字节，就在数据的末尾填充几个字节的几，比如缺少5个字节，就在末尾填充5个字节的5。PKCS7是指分组数据缺少几个字节，就在数据的末尾填充几个字节的0，比如缺少7个字节，就在末尾填充7个字节的0。NoPadding是指不需要填充，也就是说数据的发送方肯定会保证最后一段数据也正好是16个字节。那如果在PKCS5模式下，最后一段数据的内容刚好就是16个16怎么办？那解密端就不知道这一段数据到底是有效数据还是填充数据了，因此对于这种情况，PKCS5模式会自动帮我们在最后一段数据后再添加16个字节的数据，而且填充数据也是16个16，这样解密段就能知道谁是有效数据谁是填充数据了。PKCS7最后一段数据的内容是16个0，也是同样的道理。**解密端需要使用和加密端同样的Padding模式，才能准确的识别有效数据和填充数据。我们开发通常采用PKCS7 Padding模式。**
- **初始向量IV**：**初始向量IV的作用是使加密更加安全可靠，我们使用AES加密时需要主动提供初始向量，而且只需要提供一个初始向量就够了，后面每段数据的加密向量都是前面一段的密文。初始向量IV的长度规定为128位16个字节，初始向量的来源为随机生成。**至于为什么初始向量能使加密更安全可靠，会在下面的加密模式中提到。
- **密钥**：AES要求密钥的长度可以是128位16个字节、192位或者256位，位数越高，加密强度自然越大，但是加密的效率自然会低一些，因此要做好衡量。**我们开发通常采用128位16个字节的密钥，我们使用AES加密时需要主动提供密钥，而且只需要提供一个密钥就够了，每段数据加密使用的都是这一个密钥，密钥来源为随机生成。**（注意：后面我们在谈到AES的加密原理时，会提到一个初始密钥，那个初始密钥指的就是这里我们提供的这个密钥。）
- **四种加密模式**：AES一共有四种加密模式，分别是**ECB（电子密码本模式）**、**CBC（密码分组链接模式）**、**CFB**、**OFB**，**我们一般使用的是CBC模式**。四种模式中除了ECB相对不安全之外，其它三种模式的区别并没有那么大，因此这里只会对ECB和CBC模式做一下简单的对比，看看它们在做什么。
  - **ECB（电子密码本模式）**

![img](https:////upload-images.jianshu.io/upload_images/1116725-60f56a559264f7a7.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/782/format/webp)

ECB模式是最基本的加密模式，即仅仅使用明文和密钥来加密数据，相同的明文块会被加密成相同的密文块，这样明文和密文的结构将是完全一样的，就会更容易被破解，相对来说不是那么安全，因此很少使用。

- **CBC（密码分组链接模式）**

![img](https:////upload-images.jianshu.io/upload_images/1116725-76f308b0bdbaa71d.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/583/format/webp)

而CBC模式则比ECB模式多了一个初始向量IV，加密的时候，第一个明文块会首先和初始向量IV做异或操作，然后再经过密钥加密，然后第一个密文块又会作为第二个明文块的加密向量来异或，依次类推下去，这样相同的明文块加密出的密文块就是不同的，明文的结构和密文的结构也将是不同的，因此更加安全，我们常用的就是CBC加密模式。

- **CFB、OFB**：略。

###### （2）AES的加密流程

经过了（1）五个关键词的讲解，我们可以先说一句话：**我们通常使用AES加密，会采用128位16个字节的密钥和CBC加密模式。**

因此，我们这里要说的加密流程就是针对这种情况的。让我们再把CBC加密模式的图托到这里，重新整理一下AES的加密流程：

![img](https:////upload-images.jianshu.io/upload_images/1116725-6030d11aafed89c5.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/583/format/webp)

- 首先AES加密会把明文按128位16个字节，切成一段一段的数据，如果数据的最后一段不够16个字节，会用Padding来填充。
- 然后把明文块0与初始向量IV做异或操作，再用密钥加密，得到密文块0，同时密文块0也会被用作明文块1的加密向量。
- 然后明文块1与密文块0进行异或操作，再用密钥加密，得到密文块1。（当然这里只是假设数据只有两段，如果数据不止两段，依次类推，就可以得到很多段密文块。）
- 最后把密文块拼接起来就能得到最终的密文。

###### 3、AES的加密原理

我们探讨AES的加密原理，其实就是探讨上图中**加密器里面做了什么**。那加密器里面做了什么呢？这里不妨先透漏一下，加密器里其实做了四个重要的操作，分别是：**密钥扩展**、**初始轮**、**重复轮**和**最终轮**。**AES的加密原理其实可以说，就是这四个操作。**

好了，AES已经休息够了，它要开始了。

- **密钥扩展**：密钥扩展是指根据初始密钥生成后面10轮密钥的操作。（为什么是10轮，后面重复轮会说到）

![img](https:////upload-images.jianshu.io/upload_images/1116725-b3a93d2c3b7cf6ee.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 为什么需要进行密钥扩展？
>
> 因为AES加密内部其实不只执行一轮加密，而是一共会执行11轮加密，所以AES会通过一个简单快速的混合操作，根据初始密钥依次生成后面10轮的密钥，每一轮的密钥都是依据上一轮生成的，所以每一轮的密钥都是不同的。尽管很多人抱怨说这种方式太简单了，但其实已经足够安全了。

![img](https:////upload-images.jianshu.io/upload_images/1116725-5c8b5e4ba0ee1949.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)



![img](https:////upload-images.jianshu.io/upload_images/1116725-f91ba1ad60979dfb.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)



![img](https:////upload-images.jianshu.io/upload_images/1116725-9263ad6f16f77707.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 那密钥扩展具体怎么个扩展法呢？
>
> 首先我们要知道**除了初始密钥以外，后面每一轮的密钥都是由上一轮的密钥扩展而来的，密钥扩展有四个步骤：排列、置换、与轮常量异或、生成下一轮密钥的其他列。**排列是指对数据重新进行安排，置换是指把数据映射为其它的数据，轮常量异或是指......，生成下一轮密钥的其他列是指......。
>
> 比方说我们现在要扩展出第二轮的密钥：
>
> **第一步排列**：我们会拿出初始密钥的最后一列（密钥为16个字节，请自行将字节和格子对应起来看），然后把这一列的第一个字节放到最后一个字节的位置上去，其它字节依次向上移动一位，我们称经过排列后这一列为**排列列**。
>
> **第二步置换**：然后把排列列经过一个置换盒（即S盒），排列列就会被映射为一个崭新的列，我们称这个崭新的列为**置换列**。
>
> **第三步与轮常量异或**：然后我们会把置换列和一个叫轮常量的东西相异或，这样初始密钥的最后一列经过三个步骤，就成为了一个崭新的列，这一列将用来作为第二轮密钥的最后一列。
>
> **第四步生成二轮密钥的其他列**：很简单，刚才不是已经得到二轮密钥的最后一列了嘛，然后用二轮密钥的最后一列和初始密钥的第一列异或就得到二轮密钥的第一列，用二轮密钥的第一列和初始密钥的第二列异或就得到二轮密钥的第二列，用二轮密钥的第二列和初始密钥第三列异或就得到二轮密钥的第三列，这样二轮密钥的四列就集齐了，我们就可以得到一个完整的128位16字节的二轮密钥。（256位密钥要比这个复杂的多）

> 这样一轮密钥就算扩展完了，依照这样的方法，我们就可以由二轮密钥扩展出三轮密钥，由三轮密钥扩展出四轮密钥，以此类推，直至扩展出后面需要的**10轮密钥**。

- **初始轮**：初始轮就是将128位的明文数据与128位的初始密钥进行异或操作。

![img](https:////upload-images.jianshu.io/upload_images/1116725-76b9bbc2aad3160a.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 现在假设我们的明文数据为15字节，并且用Padding填充了最后一个字节，我们把这16个字节的数据放在一个4X4的矩阵里，这个矩阵我们称之为**状态矩阵**，下文中我们说到的状态矩阵就是指这个矩阵。

![img](https:////upload-images.jianshu.io/upload_images/1116725-299a9febbabfd0ad.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 初始轮就是将128位的明文数据与128位的初始密钥进行异或操作。

- **重复轮**：所谓重复轮，就是指把字节混淆、行移位、列混乱、加轮密钥这四个操作重复执行好几轮。

![img](https:////upload-images.jianshu.io/upload_images/1116725-eeca0594a097028c.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 接下来，我们就要开始重复轮了。**所谓重复轮就是指对一些操作重复执行好几轮。**重复轮重复的轮数取决于密钥的长度，**128位16字节的密钥重复轮推荐重复执行9次**，192位密钥重复轮推荐重复执行11次，256位密钥重复轮推荐重复执行13次。
>
> 那么每一轮具体要重复什么操作呢？
> **重复轮每轮重复的操作包括：字节混淆、行移位、列混乱、加轮密钥，**下面会分别介绍。

- **字节混淆**

![img](https:////upload-images.jianshu.io/upload_images/1116725-ebb7691c494c5f18.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 我们把初始轮得到的状态矩阵经过一个置换盒，会输出一个新的矩阵，我们这里叫它为字节混淆矩阵。

- **行移位**

![img](https:////upload-images.jianshu.io/upload_images/1116725-107d8de9aeac9f42.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

aes_act_3_scene_12_shift_rows_1100.png

> 按图左的方式，对字节混淆矩阵进行行移位，然后再按照图右的方式重新放一下字节，这样行移位就算完成，得到的新矩阵，我们这里称之为行移位矩阵。

- **列混乱**

![img](https:////upload-images.jianshu.io/upload_images/1116725-83f43212089ddcc0.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 然后用模不可约多项式将每列混乱，得到一个新的矩阵，我们称之为列混乱矩阵。

- **加轮密钥（AddRoundKey）**

![img](https:////upload-images.jianshu.io/upload_images/1116725-30c035e0c1dbfccc.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 在每一轮结束的时候，我们需要把列混乱矩阵和下一轮的密钥做一下异或操作，得到一个新的矩阵，我们这里称之为加轮秘钥矩阵。

> **128位密钥重复轮重复执行9次**：其实这个加轮秘钥矩阵就是下一轮的状态矩阵，拿着这个新的状态矩阵返回去，重复执行字节混淆、行移位、列混乱、加轮密钥这四个操作9次，就会进入加密的最终轮了。

- **最终轮**：最终轮其实和重复轮的操作差不多，只是在最终轮我们丢弃了列混乱这个操作

![img](https:////upload-images.jianshu.io/upload_images/1116725-67f8ad616f8d599e.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 最终轮其实和重复轮的操作差不多，只是在最终轮我们丢弃了列混乱这个操作，因为我们不会再有下一轮了，所以没必要再进行列混乱，再进行的话也加强不了安全性了，只会白白的浪费时间、拖延加密效率。

> 最终轮结束后，我们就算完成了一次AES加密，我们就会得到一块明文数据的密文了。

- **4、总结**

![img](https:////upload-images.jianshu.io/upload_images/1116725-76df419f4f51eb4f.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 所以，每执行一次AES加密，其实**内部一共进行了11轮加密**，包括1个初始轮，9个拥有4个操作的重复轮，1个拥有3个操作的最终轮，才算得到密文。

![img](https:////upload-images.jianshu.io/upload_images/1116725-c5e3f2640bbbfc3e.png?imageMogr2/auto-orient/strip|imageView2/2/w/1100/format/webp)

> 解密意味着加密的逆过程，我们只需要把加密的每个步骤倒着顺序执行就能完成解密了。

**至于AES加密更深一层的数学原理，可参看参考博客，里面有详细介绍。**

###### 4、AES加密的代码



```objectivec
//
//  AES128Encrypt.h
//  EncryptDemo
//
//  Created by 意一yiyi on 2018/5/31.
//  Copyright © 2018年 意一yiyi. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AES128Encrypt : NSObject

/**
 * AES128加密，Base64编码输出
 *
 * @param plainText 明文
 * @param secretKey 密钥
 * @param iv 初始向量
 *
 * @return AES128加密后的密文
 */
+ (NSString *)aes128CiphertextFromPlainText:(NSString *)plainText secretKey:(NSString *)secretKey iv:(NSString *)iv;

/**
 * AES128解密，Base64编码输入
 *
 * @param ciphertext 密文
 * @param secretKey 密钥
 * @param iv 初始向量
 *
 * @return AES128解密后的明文
 */
+ (NSString *)aes128PlainTextFromCiphertext:(NSString *)ciphertext secretKey:(NSString *)secretKey iv:(NSString *)iv;

@end
```



```objectivec
//
//  AES128Encrypt.m
//  EncryptDemo
//
//  Created by 意一yiyi on 2018/5/31.
//  Copyright © 2018年 意一yiyi. All rights reserved.
//

#import "AES128Encrypt.h"
#import <CommonCrypto/CommonCryptor.h>

@implementation AES128Encrypt

/**
 * AES128加密，Base64编码输出
 *
 * @param plainText 明文
 * @param secretKey 密钥
 *
 * @return AES128加密后的密文
 */
+ (NSString *)aes128CiphertextFromPlainText:(NSString *)plainText secretKey:(NSString *)secretKey iv:(NSString *)iv {
    
    char keyPtr[kCCKeySizeAES128 + 1];
    memset(keyPtr, 0, sizeof(keyPtr));
    [secretKey getCString:keyPtr maxLength:sizeof(keyPtr) encoding:NSUTF8StringEncoding];
    
    char ivPtr[kCCBlockSizeAES128 + 1];
    memset(ivPtr, 0, sizeof(ivPtr));
    [iv getCString:ivPtr maxLength:sizeof(ivPtr) encoding:NSUTF8StringEncoding];
    
    NSData *data = [plainText dataUsingEncoding:NSUTF8StringEncoding];
    NSUInteger dataLength = [data length];
    
    int diff = kCCKeySizeAES128 - (dataLength % kCCKeySizeAES128);
    NSUInteger newSize = 0;
    
    if(diff > 0) {
        
        newSize = dataLength + diff;
    }
    
    char dataPtr[newSize];
    memcpy(dataPtr, [data bytes], [data length]);
    for(int i = 0; i < diff; i ++) {
        
        dataPtr[i + dataLength] = 0x00;
    }
    
    size_t bufferSize = newSize + kCCBlockSizeAES128;
    void *buffer = malloc(bufferSize);
    memset(buffer, 0, bufferSize);
    
    size_t numBytesCrypted = 0;
    
    CCCryptorStatus cryptStatus = CCCrypt(kCCEncrypt,// 加密
                                          kCCAlgorithmAES128,// AES128加密
                                          kCCOptionPKCS7Padding,// PKCS7 Padding模式
                                          keyPtr,// 密钥
                                          kCCKeySizeAES128,// 密钥长度
                                          ivPtr,// 初始向量
                                          dataPtr,
                                          sizeof(dataPtr),
                                          buffer,
                                          bufferSize,
                                          &numBytesCrypted);
    if (cryptStatus == kCCSuccess) {
        
        NSData *resultData = [NSData dataWithBytesNoCopy:buffer length:numBytesCrypted];
        // 转换成Base64并返回
        return [resultData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
    }
    free(buffer);
    return nil;
}

/**
 * AES128解密，Base64编码输入
 *
 * @param ciphertext 密文
 * @param secretKey 密钥
 * @param iv 初始向量
 *
 * @return AES128解密后的明文
 */
+ (NSString *)aes128PlainTextFromCiphertext:(NSString *)ciphertext secretKey:(NSString *)secretKey iv:(NSString *)iv {
    
    char keyPtr[kCCKeySizeAES128 + 1];
    memset(keyPtr, 0, sizeof(keyPtr));
    [secretKey getCString:keyPtr maxLength:sizeof(keyPtr) encoding:NSUTF8StringEncoding];
    
    char ivPtr[kCCBlockSizeAES128 + 1];
    memset(ivPtr, 0, sizeof(ivPtr));
    [iv getCString:ivPtr maxLength:sizeof(ivPtr) encoding:NSUTF8StringEncoding];
    
    NSData *data = [[NSData alloc] initWithBase64EncodedData:[ciphertext dataUsingEncoding:NSUTF8StringEncoding] options:NSDataBase64DecodingIgnoreUnknownCharacters];
    NSUInteger dataLength = [data length];
    size_t bufferSize = dataLength + kCCBlockSizeAES128;
    void *buffer = malloc(bufferSize);
    
    size_t numBytesCrypted = 0;
    CCCryptorStatus cryptStatus = CCCrypt(kCCDecrypt,// 解密
                                          kCCAlgorithmAES128,
                                          kCCOptionPKCS7Padding,
                                          keyPtr,
                                          kCCBlockSizeAES128,
                                          ivPtr,
                                          [data bytes],
                                          dataLength,
                                          buffer,
                                          bufferSize,
                                          &numBytesCrypted);
    if (cryptStatus == kCCSuccess) {
        
        NSData *resultData = [NSData dataWithBytesNoCopy:buffer length:numBytesCrypted];
        // 转换成普通字符串并返回
        return [[NSString alloc] initWithData:resultData encoding:NSUTF8StringEncoding];
    }
    free(buffer);
    return nil;
}

@end
```

###### 5、实际开发中使用AES加密需要注意的地方

- 服务端和我们客户端必须使用**一样的密钥和初始向量IV**。
- 服务端和我们客户端必须使用**一样的加密模式**。
- 服务端和我们客户端必须使用**一样的Padding模式**。

以上三条有一个不满足，双方就无法完成互相加解密。

同时针对对称加密**密钥传输问题**这个不足：我们一般采用**RSA+AES加密相结合的方式**，用AES加密数据，而用RSA加密AES的密钥。同时密钥和IV可以随机生成，这要是128位16个字节就行，但是**必须由服务端来生成**，因为如果由我们客户端生成的话，就好比我们客户端存放了非对称加密的私钥一样，这样容易被反编译，不安全，一定要从服务端请求密钥和初始向量IV。