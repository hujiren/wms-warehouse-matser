
初始
  git init

  git add .

## 连接git库
   git remote add origin https://github.com/arranapl/apl-wms-warehouse.git


提交描述

  git commit -m "描述"


从git库拉取代码
  git pull   origin master
  git pull -f  origin master
  git pull -u  origin master


合并代码
   自动合并
   解决冲突  vcs/git


推送代码到git库

  git push origin master
  
  git push -f origin master
  
  git push -u origin master
  
  




解决.gitignore文件不生效
git rm -r --cached .
git add .
git commit -m 'update .gitignore'



