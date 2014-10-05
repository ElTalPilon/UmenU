# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20141003194024) do

  create_table "comentarios", force: true do |t|
    t.integer  "usuario_id"
    t.integer  "plato_id"
    t.string   "comentario"
    t.integer  "puntos"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "platos", force: true do |t|
    t.integer  "soda_id"
    t.string   "nombre"
    t.integer  "precio"
    t.string   "categoria"
    t.string   "tipo"
    t.integer  "calificaciones"
    t.integer  "total"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "snacks", force: true do |t|
    t.integer  "soda_id"
    t.string   "nombre"
    t.integer  "precio"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "sodas", force: true do |t|
    t.string   "nombre"
    t.time     "abre"
    t.time     "cierra"
    t.time     "iDesayuno"
    t.time     "fDesayuno"
    t.time     "iAlmuerzo"
    t.time     "fAlmuerzo"
    t.time     "iCena"
    t.time     "fCena"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.float    "long"
    t.float    "lat"
  end

  create_table "usuarios", force: true do |t|
    t.string   "nombre"
    t.string   "direccion"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end
