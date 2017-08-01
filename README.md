# STM

The implementations of the STM topic models, as described in 2016 CIKM paper:

**Effective Document Labeling with Very Few Seed Words:
A Topic Modeling Approach.**
Chenliang Li, Jian Xing, Aixin Sun, Zongyang Ma.

## Description
1. catalog file

Catalog file is uesd to describe category information. For example, the catalog file of data set '20 news groups' sholud be wrote in this form :

cate
alt.atheism
cate
comp.graphics
cate
comp.windows.x
.....

If we want to combine several categories into one , take 'comp' and 'sci' as examples , then the file should be wrote like this :

cate
comp.graphics
comp.os.ms-windows.misc
comp.sys.ibm.pc.hardware
comp.windows.x
comp.sys.mac.hardware
cate
sci.crypt
sci.med
sci.space
sci.electronics
.....

remember that data file path must be same as category name.
"20_news_group/comp.graphics/..." : a path like this is valid 
"20_news_group/othernames/..." : and this one may cause run-time error

2. seed word file

Just like catalog file , seed word file also use 'cate' as sign of the begin of a category :

cate
windows dos microsoft ms
cate
graphics image gif animation tiff
cate
gay homosexual sexual
....

Make sure that the category order in catalog file is same as it in seed word file, like this:

cate                cate
comp.graphics       graphics image gif animation tiff
cate                cate
sci.space           space orbit moon earth sky solar
.....               .....

3. launch the program 

The main Java entry is in class 'stm'. To launch the program there are several parameters must be specified , such like "model.testSetPath"
which means the path of test path , and 'model.catalogPath' which means the path of catalog file.


