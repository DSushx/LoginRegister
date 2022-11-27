from os.path import dirname, join
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity


def main(fod,usr):

  filename = join(dirname (__file__),"food.csv")
  df = pd.read_csv(filename)
  df['早餐']=df.apply(if_breakfast,axis=1)

  value=df.groupby('早餐').mean().鈉
  avg_bre=value[1] #早餐鈉平均
  avg_din=value[0]

  def lows(row):
    if(row['早餐']==1):
      if(row['鈉']<avg_bre):
        return 1
      else:
        return 0
    else:
      if(row['鈉']<avg_din):
        return 1
      else:
        return 0

  df['低鈉食物']=df.apply(lows,axis=1)

  value=df.groupby('早餐').mean().糖
  avgsug_bre=value[1] #早餐熱量平均
  avgsug_dinn=value[0] #午晚餐熱量平均
  def if_lowsug(row):
    if(row['早餐']==1):
      if(row['糖']<avgsug_bre):
        return 1
      else:
        return 0
    else:
      if(row['糖']<avgsug_dinn):
        return 1
      else:
        return 0

  df['低糖食物']=df.apply(if_lowsug,axis=1)
  userid=int(usr)
  filename2 = join(dirname (__file__),"user.csv")
  df2=pd.read_csv(filename2)
  if(df2.at[userid,'高血壓']==1):
    df = df[df.低鈉食物 != 1]

  if(df2.at[userid,'糖尿病']==1):
    df = df[df.低糖食物 != 1]

  def create_tags(x):
      tags = x['tags'].split('、')
      #tags.extend(x['title'].split())
      #tags.extend(x['category'].split())
      return " ".join(sorted(set(tags), key=tags.index))

  df['mixtags'] = df.apply(create_tags, axis=1)


  #CountVectorizer中文特徵提取
  count = CountVectorizer()
  #轉成特徵向量
  count_matrix = count.fit_transform(df['mixtags'])


  #把兩個食物tags的特徵向量
  cosine_sim = cosine_similarity(count_matrix, count_matrix)
  indices_from_title = pd.Series(df.index, index=df['title'])
  indices_from_food_id = pd.Series(df.index, index=df['food_id'])

  def get_recommendations(title="", cosine_sim=cosine_sim, idx=-1):

      if idx == -1 and title != "":
          idx = indices_from_title[title]

      sim_scores = list(enumerate(cosine_sim[idx]))
      sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
      sim_scores = sim_scores[1:4]
      food_indices = [i[0] for i in sim_scores]
      return food_indices


  #return df.loc[get_recommendations(title=fod)]
  return df.loc[get_recommendations(title=fod)]




def if_breakfast(row):
  if row['category']=='飯糰':
    return 1
  if row['category']=='三明治':
    return 1
  if row['category']=='麵包':
    return 1
  else:
    return 0



