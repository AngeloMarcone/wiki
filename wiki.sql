PGDMP      6                |            wiki2.0    16.1    16.1 '    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    41190    wiki2.0    DATABASE     �   CREATE DATABASE "wiki2.0" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE "wiki2.0";
                postgres    false            �            1255    49185 .   aggiorna_caratteri_dopo_inserimento_modifica()    FUNCTION     �  CREATE FUNCTION public.aggiorna_caratteri_dopo_inserimento_modifica() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.utente = (SELECT utente FROM autore WHERE nomedarte = NEW.autore) THEN
        UPDATE Frase
        SET caratteri = NEW.frasemodificata
        WHERE codfrase = NEW.codfrase AND pagina = NEW.pagina;
		
		UPDATE Modifica
        SET stato = 1
        WHERE codmod = NEW.codmod;
		
    END IF; 
    RETURN NEW;
END;
$$;
 E   DROP FUNCTION public.aggiorna_caratteri_dopo_inserimento_modifica();
       public          postgres    false            �            1255    49165 %   aggiorna_pagina_dopo_modifica_stato()    FUNCTION     1  CREATE FUNCTION public.aggiorna_pagina_dopo_modifica_stato() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.stato = 1 THEN
        UPDATE Frase
        SET caratteri = NEW.frasemodificata
        WHERE codfrase = NEW.codfrase AND pagina = NEW.pagina;
    END IF;
    RETURN NEW;
END;
$$;
 <   DROP FUNCTION public.aggiorna_pagina_dopo_modifica_stato();
       public          postgres    false            �            1259    41198    autore    TABLE     �   CREATE TABLE public.autore (
    nomedarte character varying(128) NOT NULL,
    annoiniziocarriera date NOT NULL,
    utente character varying(128) NOT NULL
);
    DROP TABLE public.autore;
       public         heap    postgres    false            �            1259    49170    collegamento    TABLE     �   CREATE TABLE public.collegamento (
    codfrase integer,
    pagina_frase character varying(128),
    pagina_riferimento character varying(128)
);
     DROP TABLE public.collegamento;
       public         heap    postgres    false            �            1259    41223    frase    TABLE     �   CREATE TABLE public.frase (
    codfrase integer NOT NULL,
    caratteri character varying(1000),
    pagina character varying(128) NOT NULL
);
    DROP TABLE public.frase;
       public         heap    postgres    false            �            1259    73728    modifica_id_sequence    SEQUENCE     }   CREATE SEQUENCE public.modifica_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.modifica_id_sequence;
       public          postgres    false            �            1259    41235    modifica    TABLE     �  CREATE TABLE public.modifica (
    codmod integer DEFAULT nextval('public.modifica_id_sequence'::regclass) NOT NULL,
    fraseoriginale character varying(1000),
    codfrase integer NOT NULL,
    pagina character varying(128) NOT NULL,
    frasemodificata character varying(1000),
    stato integer,
    dataoramod timestamp without time zone NOT NULL,
    utente character varying(128) NOT NULL,
    autore character varying(128) NOT NULL
);
    DROP TABLE public.modifica;
       public         heap    postgres    false    222            �            1259    41213    pagina    TABLE     �   CREATE TABLE public.pagina (
    link character varying(128) NOT NULL,
    titolo character varying(128) NOT NULL,
    dataoracreazione timestamp without time zone NOT NULL,
    autore character varying(128) NOT NULL
);
    DROP TABLE public.pagina;
       public         heap    postgres    false            �            1259    41208    stato    TABLE     X   CREATE TABLE public.stato (
    id integer NOT NULL,
    stato character varying(20)
);
    DROP TABLE public.stato;
       public         heap    postgres    false            �            1259    41191    utente    TABLE     �  CREATE TABLE public.utente (
    mail character varying(128) NOT NULL,
    nome character varying(128) NOT NULL,
    cognome character varying(128) NOT NULL,
    password character varying(128) NOT NULL,
    CONSTRAINT check_password_valida CHECK ((((password)::text ~ '[A-Z]'::text) AND ((password)::text ~ '[a-z]'::text) AND ((password)::text ~ '[0-9]'::text) AND (length((password)::text) >= 8))),
    CONSTRAINT checkemail_valida CHECK (((mail)::text ~~ '%@%.%'::text))
);
    DROP TABLE public.utente;
       public         heap    postgres    false            �          0    41198    autore 
   TABLE DATA           G   COPY public.autore (nomedarte, annoiniziocarriera, utente) FROM stdin;
    public          postgres    false    216   3       �          0    49170    collegamento 
   TABLE DATA           R   COPY public.collegamento (codfrase, pagina_frase, pagina_riferimento) FROM stdin;
    public          postgres    false    221   {4       �          0    41223    frase 
   TABLE DATA           <   COPY public.frase (codfrase, caratteri, pagina) FROM stdin;
    public          postgres    false    219   �4       �          0    41235    modifica 
   TABLE DATA           �   COPY public.modifica (codmod, fraseoriginale, codfrase, pagina, frasemodificata, stato, dataoramod, utente, autore) FROM stdin;
    public          postgres    false    220   :       �          0    41213    pagina 
   TABLE DATA           H   COPY public.pagina (link, titolo, dataoracreazione, autore) FROM stdin;
    public          postgres    false    218   �;       �          0    41208    stato 
   TABLE DATA           *   COPY public.stato (id, stato) FROM stdin;
    public          postgres    false    217   �<       �          0    41191    utente 
   TABLE DATA           ?   COPY public.utente (mail, nome, cognome, password) FROM stdin;
    public          postgres    false    215   �<       �           0    0    modifica_id_sequence    SEQUENCE SET     C   SELECT pg_catalog.setval('public.modifica_id_sequence', 39, true);
          public          postgres    false    222            :           2606    41202    autore autore_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.autore
    ADD CONSTRAINT autore_pkey PRIMARY KEY (nomedarte);
 <   ALTER TABLE ONLY public.autore DROP CONSTRAINT autore_pkey;
       public            postgres    false    216            @           2606    41229    frase frase_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.frase
    ADD CONSTRAINT frase_pkey PRIMARY KEY (codfrase, pagina);
 :   ALTER TABLE ONLY public.frase DROP CONSTRAINT frase_pkey;
       public            postgres    false    219    219            B           2606    73732    modifica modifica_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.modifica
    ADD CONSTRAINT modifica_pkey PRIMARY KEY (codmod);
 @   ALTER TABLE ONLY public.modifica DROP CONSTRAINT modifica_pkey;
       public            postgres    false    220            >           2606    41217    pagina pagina_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT pagina_pkey PRIMARY KEY (link);
 <   ALTER TABLE ONLY public.pagina DROP CONSTRAINT pagina_pkey;
       public            postgres    false    218            <           2606    41212    stato stato_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.stato
    ADD CONSTRAINT stato_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.stato DROP CONSTRAINT stato_pkey;
       public            postgres    false    217            8           2606    41197    utente utente_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (mail);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    215            L           2620    81925 #   modifica aggiorna_caratteri_trigger    TRIGGER     �   CREATE TRIGGER aggiorna_caratteri_trigger AFTER INSERT ON public.modifica FOR EACH ROW EXECUTE FUNCTION public.aggiorna_caratteri_dopo_inserimento_modifica();
 <   DROP TRIGGER aggiorna_caratteri_trigger ON public.modifica;
       public          postgres    false    223    220            M           2620    49166     modifica aggiorna_pagina_trigger    TRIGGER     �   CREATE TRIGGER aggiorna_pagina_trigger AFTER UPDATE ON public.modifica FOR EACH ROW EXECUTE FUNCTION public.aggiorna_pagina_dopo_modifica_stato();
 9   DROP TRIGGER aggiorna_pagina_trigger ON public.modifica;
       public          postgres    false    220    224            C           2606    41203    autore autore_utente_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.autore
    ADD CONSTRAINT autore_utente_fkey FOREIGN KEY (utente) REFERENCES public.utente(mail);
 C   ALTER TABLE ONLY public.autore DROP CONSTRAINT autore_utente_fkey;
       public          postgres    false    4664    216    215            J           2606    49173 4   collegamento collegamento_codfrase_pagina_frase_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_codfrase_pagina_frase_fkey FOREIGN KEY (codfrase, pagina_frase) REFERENCES public.frase(codfrase, pagina);
 ^   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_codfrase_pagina_frase_fkey;
       public          postgres    false    219    219    221    221    4672            K           2606    49178 1   collegamento collegamento_pagina_riferimento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_pagina_riferimento_fkey FOREIGN KEY (pagina_riferimento) REFERENCES public.pagina(link);
 [   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_pagina_riferimento_fkey;
       public          postgres    false    221    218    4670            E           2606    41230    frase frase_pagina_fkey    FK CONSTRAINT     x   ALTER TABLE ONLY public.frase
    ADD CONSTRAINT frase_pagina_fkey FOREIGN KEY (pagina) REFERENCES public.pagina(link);
 A   ALTER TABLE ONLY public.frase DROP CONSTRAINT frase_pagina_fkey;
       public          postgres    false    218    219    4670            F           2606    41247    modifica modifica_autore_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.modifica
    ADD CONSTRAINT modifica_autore_fkey FOREIGN KEY (autore) REFERENCES public.autore(nomedarte);
 G   ALTER TABLE ONLY public.modifica DROP CONSTRAINT modifica_autore_fkey;
       public          postgres    false    216    4666    220            G           2606    41257 &   modifica modifica_codfrase_pagina_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.modifica
    ADD CONSTRAINT modifica_codfrase_pagina_fkey FOREIGN KEY (codfrase, pagina) REFERENCES public.frase(codfrase, pagina);
 P   ALTER TABLE ONLY public.modifica DROP CONSTRAINT modifica_codfrase_pagina_fkey;
       public          postgres    false    219    219    4672    220    220            H           2606    41252    modifica modifica_stato_fkey    FK CONSTRAINT     y   ALTER TABLE ONLY public.modifica
    ADD CONSTRAINT modifica_stato_fkey FOREIGN KEY (stato) REFERENCES public.stato(id);
 F   ALTER TABLE ONLY public.modifica DROP CONSTRAINT modifica_stato_fkey;
       public          postgres    false    217    4668    220            I           2606    41242    modifica modifica_utente_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.modifica
    ADD CONSTRAINT modifica_utente_fkey FOREIGN KEY (utente) REFERENCES public.utente(mail);
 G   ALTER TABLE ONLY public.modifica DROP CONSTRAINT modifica_utente_fkey;
       public          postgres    false    220    215    4664            D           2606    41218    pagina pagina_autore_fkey    FK CONSTRAINT        ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT pagina_autore_fkey FOREIGN KEY (autore) REFERENCES public.autore(nomedarte);
 C   ALTER TABLE ONLY public.pagina DROP CONSTRAINT pagina_autore_fkey;
       public          postgres    false    218    216    4666            �   Q  x�U���� ���.0Ъko<�^ƚ)��k}����:��˗����0(�(�j/t��
�tMzL�ő��M1����Q셌;9��;�b���uBk�[�BfʐQ懼M�;HH�\����:��f$�Hj�t����@�Z�F�
ݱ���������)g,����Qώm�Ixr)N��_mtj]sU����Xki�is��g�4[�	�}�י�-vUk�:B+�B&�I?sk�����VcD�goh�#��S��(Tm�|K#�X�'ӏ@~1{��w_n�d����L�쏽'��hې%�<�qӰ\��Z֏��B����m��}Y�#؂6ʯ���{�      �   6   x�3�,�����K����K�3AL.c��Ԣ�DGtq#��Ĝ�L�z��=... �b      �   �  x�}V=o�F��_�p#	���lKqeX�!�v�8�(*��s� �.��~�ˤt�2E�t�U�Sy3�(� ����̛yof�~����e��xy�l}0X,-�L�/ޱ5��?�O�������������i�7.tœ�l[��5���#�pqd��>�����d�� N.t��)�>���p�4��9�ao�r�RB{Զ��@��2Om�[�v�����;k�|��(G1)n�!��cA ��	2=��38���&�l��4����Dw�a��w���E5};��_�O��+�/0���0�|;:��U19Z<#t��]��l��m �y$p��
p�w�$އLk�eb�@�Qľ���N*O �
D�vi�x�sׂ$��������Ł>�۶-�4�O:J�_���J���GN��(Α��n3��*,��� ,��f#K�+��di�`��@g��tql:��"v�O��Y��"U���`0���3��X�Γ����_a��yYn�2`���g�()}��������z���F�pBƂ&w�,��b*��)��jш"�c�PKn�)�YC��v��]�i�Z�q�
�s��œ�8h�������e�5�6�9n�~�1cd�j�nw��jy�o~�x	�C�T���9:;;���4	�6�+ I}�)D�Uw�.��DHFgS�ʭ�ZR+�4+p#g%�1G��M;�Cb�,q׌6u��2�$G2`�r^Pc���-*¼�c|�|���m�}����7FN��N��s��t4P�D�&�g�r��5�"����P%=;���:^����:����������Y�AE��f�5�+�8Rx.ܰ��5 d���rD��4	)�H}I��~��9 QEB/S��qW�3)�[��P�U��Q�ʗZ�D�S}h��65�X�[h(u (�$�HEgԠ�ˁ`"et�f�/'�"�	�W�ˑY��B�������qm��v|��	I�1��]�Ef) |N�d�-AdW�^*�g��������tH�b^�\��9#I+,�5�����Mr��۽Pqʓ���	9d�>�vRM�T�qX��I2ܢ�(n�8��c���D�d�)ҽC,X�
8�un ��:2�h����I�kǹ��F46c�Vʾr`Z���D��w9��,0�X{0�y凣C������c�����b4��=�G��[�"��
�0��>�p+�� �Ԣ�TЩ��k��Ӈg��w[��[�2�JL(�J}�5�=���"o�?O%D��X��a֑��G��p�E�4�6�@lH���E`��K��1����A��89���xW�I �-[�d^n{)�N	 �h��O�;fFS�TR*
`H�-r���2����֔��i�6K�]�.���] �}�l>�~,�`a�cav%m�|���ɓ� +��J;��Ӹ5٪�:0��b.���x��*�3\����U���Q\�i?������� �      �   3  x��Q�n�0������X����U�.�,W�Г;uL�����X*���������&�.��9C��3O@	�/��	cB��<B҄^��<�`Wp�~޴��|2"���P��ֹZ���-�V7Ro�8Rx=�Hn�1N�y��Zuͳ^�;��-�T��1h-v���1�0�N��ڐ�>M�>D<�-]g������a4tJv�cc���%(�b��R��������l��Z6y�0����f���F>��2�]��c?0�e����m��t�z����4�0y����C-���Vm�.��I�e�eq<�      �   �   x�e��j!E��W����Xw!�ٕB���a%�����1&�0g�9W�r��y�����	��8YP��7���������1w�@1�ϳ/5�sY�E)Jm�%P�h��w�;��i^˦�̄�.�ҕ���򇏡��-/��
�D8��g�Qt��w\+h%J��TN���_w�p�T�JDc�j��"l_��+c7�c�      �       x�3�t,((�/K,��2�����E�c���� �X
       �   �  x��VkS�:�,��B�}ݘ3PB���m2�ʲb+��HvB���~Vv�I����0�d�]k���b���e�%b�+��5)���@{>����S�d"�y.l�):�)-bz�塈9�yw@Y���;;���<< �e�'g�+��ᩏ��%�՞��E饱�Yg˸B<)��[I��!WBk
�B�6���.�:�-<a*K�?<t�\�|�gt�D��?(J�*��Ѩ�4��hr&�_%YlI�k*���:8����@��g�fb&h|D��#n3���f���[�;���kcQH��5�t.U@�W��@tZ�C�(p�;������XjD�v�|*B}L�4t�Ȃo�{��b�(�����G����?!rn�C��)�����9�;��L�����b�r/(�)M���Z�F�"s�����>{��6� ;�!	]$4,U4fB�h�u�_�~v�պ{�XΥ�B�2f���]A}�����h����n�eȓs���;IZ�Y
E�y|GE��8�B�UّG�7����_��9N��9aQfq7�+(Q#�"�9��n�?NpȨ�J'$��@�iE�Ũ�e�.�s����*�5x)��1M8�L����.q�V݋�F��^ꇻ��'�����#�fT�')g~,M�j@2%R���D�w�ۏN�4��ԑ.�� sB��A��Ե�ch�*�F���ֱ�jJ��㮌�X�5�
��}@Q�*�5�JS�}�r%݀0�Y�qWj=�Vv(��&;�����ۛ�'2���x\2�
��i�: %�E��xX�m�U�ө�G�H)�BQ�z��PI��9���������9BO�~�XB���W��
Ca�v��Ƨ�U���"�4%a>4���X�Z�3P��E��d�Ng�k<�"D��P��|�.w�T�!�V����I"�Bt3��x>q�\�k�P6�����`�����)����4��Bx2�sL��;w>��I���_�w��ה:!׫ymg�E_Vf4i�^N�����ek�P=�Lj�#�^~�5��� J��͊�+���-㌸ܚehl#����΂�����T*5ї��j��+j:gh ����j�N����%ܐ�a�̖��XL�"�͍n�x4r�F�y����#�3��HSUP�hgJj�R>��� �+�֏Ȏ�"������{P0����ߣ��1�)��+��#����؏(��5t���b~�H�Lt��z�"q_������c��|�cI��2�n,��j�@X*C��}M]��вW��y_��ٍ3���,��{\K8�a��V���t`-d{��ɉ���C���V��o��@͆z�{�o��hI�0�v�p�d��ឈ]`���˷��g�F��7؁L�Ug�d��@y�g���/�U�Ұ���_�:�k�J�iv4�JK�� h��P���d�@(h�Hc��()�.��Q�(�أQ}�l�UG�	vx�A�v�$~��V�F�H��H�+���8�V"'����IC���W
�i�0iH�1�� r;ճ��'ۦ��^��gE.5���V���/{���0��>eS'*�\n# ����bo�k�����0er��CD�eE&u\x5�T�;?�n���.�-�f3��G�P�.�?lX�@#
�錝OX�f2�n����-$%AE�n�}в_ވx�[J05��d̉[�h��]!ͫ� �jwR�uM]��B�)k��V�T�����������yBa@�K�?W.�c�<Ʉ.�r�?��r�ݡ2�Q����?�Jh-��<�R�5��܀�B���"��.ʕ��kca��=p
��ĉ��gm~���?��8�,�5�܌V�?�琞�2��5��]�O8l�x��!�^����&2�u��?Ʃ%�!���y�݆�h��/Z��dU�?8������\o�au[��`�mda��#@H�     