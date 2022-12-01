import numpy as np
import pandas as pd
from collections import defaultdict
import pymysql
from java import jclass


def main(id):

    # database connection
    connection = pymysql.connect(host="10.0.2.2", port=3306, user="root", passwd="", database="food_db")
    cursor = connection.cursor()

    # get food data
    sql1 = "SELECT * FROM food_seven"
    cursor.execute(sql1)
    food_data = pd.DataFrame(cursor.fetchall())
    food_data.columns = ['food_id', 'title', 'categories', 'tags', '重量(g)', '熱量',
                         '蛋白質(g)', '脂肪(g)', '碳水化合物(g)', '糖', '鈉', 'image']
    # print(food_data)

    # get food ratings
    sql2 = "SELECT * FROM food_rating"
    cursor.execute(sql2)
    food_ratings = pd.DataFrame(cursor.fetchall())
    food_ratings.columns = ['user_id', 'food_id', 'rating']
    # print(food_ratings)

    # close connection
    connection.close()

    # merge data
    df = pd.merge(food_ratings, food_data, on='food_id')


    # print(df)


    # function
    def recommend(user, num):
        # find similarity between user and other user.
        user_similarity = []
        for other_user in df.user_id.unique():
            if other_user == user:
                continue
            # print("other user :", other_user)
            common_foods = find_common_food(user, other_user)
            sim = cal_user_similarity_with_food_rating(user, other_user, common_foods)
            user_similarity.append([other_user, sim])

        # find top 10 similar user
        user_similarity = np.array(user_similarity)
        sorted_index = np.argsort(user_similarity, axis=0)[:, 1][::-1][:num]
        top_similar_user = user_similarity[:, 0][sorted_index]

        # find the food the user haven't eaten
        eaten_foods = df.loc[df["user_id"] == user, "food_id"].values
        # print(eaten_foods)
        not_eaten_foods = defaultdict(list)
        for similar_user in top_similar_user:
            foods = df.loc[df.user_id == similar_user, ["food_id", "rating"]].values.tolist()
            # print(foods)
            if isinstance(foods[0], list):
                for food in foods:
                    if food[0] in eaten_foods:
                        continue
                    not_eaten_foods[food[0]].append(food[1])

            elif foods[0] not in eaten_foods:
                not_eaten_foods[foods[0]].append(foods[1])

        # average same food rating
        for food in not_eaten_foods:
            not_eaten_foods[food] = np.mean(not_eaten_foods[food])

        # print(not_eaten_foods)

        # get top 10 ratings by sorting it
        top10_rating = sorted(not_eaten_foods.items(), key=lambda x: x[1], reverse=True)
        return [food for food, rating in top10_rating][:num]


    def find_common_food(user1, user2):
        """找尋兩個user共同吃過的食物"""
        s1 = set(df.loc[df["user_id"] == user1, "food_id"].values)
        s2 = set(df.loc[df["user_id"] == user2, "food_id"].values)
        return s1.intersection(s2)


    def cosine_similarity(vec1, vec2):
        """
        計算兩個向量之間的餘弦相似性
        :param vec1: 向量 a
        :param vec2: 向量 b
        :return: sim
        """
        vec1 = np.mat(vec1)
        vec2 = np.mat(vec2)
        num = float(vec1 * vec2.T)
        denom = np.linalg.norm(vec1) * np.linalg.norm(vec2)
        cos = num / denom
        np.seterr(invalid='ignore')
        sim = 0.5 + 0.5 * cos
        return sim


    def cal_user_similarity_with_food_rating(user1, user2, foods_id):
        """計算兩個user對於特定食物評分的相似度"""
        u1 = df[df["user_id"] == user1]
        u2 = df[df["user_id"] == user2]
        vec1 = u1[u1.food_id.isin(foods_id)].sort_values(by="food_id")["rating"].values
        vec2 = u2[u2.food_id.isin(foods_id)].sort_values(by="food_id")["rating"].values
        return cosine_similarity(vec1, vec2)


    # %%
    top10_food = recommend(id, num=20)
    result = food_data.loc[food_data["food_id"].isin(top10_food)]

    List = jclass("java.util.List")
    FoodData = jclass("com.example.loginregister.datasets.FoodInfo")

    for ind in result.index:


    return result